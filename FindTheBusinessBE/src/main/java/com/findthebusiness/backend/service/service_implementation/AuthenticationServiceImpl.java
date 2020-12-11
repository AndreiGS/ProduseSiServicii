package com.findthebusiness.backend.service.service_implementation;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.findthebusiness.backend.dto.authentication.*;
import com.findthebusiness.backend.entity.Authentication;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.exception.*;
import com.findthebusiness.backend.mapper.mapper_implementation.UserMapperImpl;
import com.findthebusiness.backend.repository.AuthenticationRepository;
import com.findthebusiness.backend.repository.CommentsRepository;
import com.findthebusiness.backend.repository.ShopRepository;
import com.findthebusiness.backend.repository.UserRepository;
import com.findthebusiness.backend.security.security_config.MyUserDetails;
import com.findthebusiness.backend.security.utils.AccessTokenUtil;
import com.findthebusiness.backend.security.utils.AuthenticationUtil;
import com.findthebusiness.backend.security.utils.RefreshTokenUtil;
import com.findthebusiness.backend.service.service_repository.AuthenticationService;
import com.findthebusiness.backend.utils.BasicEncrypt;
import com.findthebusiness.backend.utils.EmailUtil;
import com.findthebusiness.backend.utils.UUIDGeneratorUtil;
import com.findthebusiness.backend.utils.enums.UserRoles;
import com.google.common.hash.Hashing;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationRepository authenticationRepository;
    private final CommentsRepository commentsRepository;
    private final ShopRepository shopRepository;
    private final EmailUtil emailUtil;
    private final UserMapperImpl userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenUtil accessTokenUtil;
    private final RefreshTokenUtil refreshTokenUtil;
    private final AuthenticationUtil authenticationUtil;
    private final WebClient.Builder webClient;

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";
    private final String REFRESH_TOKEN = "REFRESH-TOKEN";
    private final String CSRF_TOKEN = "CSRF-TOKEN";

    private  String fbAppId=System.getenv("FACEBOOK_APP_ID");
    private  String fbSecret=System.getenv("FACEBOOK_SECRET");

    public AuthenticationServiceImpl(UserRepository userRepository, AuthenticationRepository authenticationRepository, CommentsRepository commentsRepository, ShopRepository shopRepository, EmailUtil emailUtil, UserMapperImpl userMapper, PasswordEncoder passwordEncoder, AccessTokenUtil accessTokenUtil, RefreshTokenUtil refreshTokenUtil, AuthenticationUtil authenticationUtil, WebClient.Builder webClient) {
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
        this.commentsRepository = commentsRepository;
        this.shopRepository = shopRepository;
        this.emailUtil = emailUtil;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenUtil = accessTokenUtil;
        this.refreshTokenUtil = refreshTokenUtil;
        this.authenticationUtil = authenticationUtil;
        this.webClient = webClient;
    }

    @Override
    public void delete7DaysOldAuthentications() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -7);
        deleteAuthenticationByDateBefore(calendar.getTime());
    }

    @Override
    public ResponseEntity<?> confirmUser(String code) throws UserNotRegisteredException {
        if(isAccessTokenExpired(code)) {
            return ResponseEntity.badRequest().build();
        }
        saveUserWithoutReturning(findUserById(getIdFromAccessToken(code)).setActive(true));
        return ResponseEntity.ok().build();
    }

    @Override
    public LoginResponseWithHttpResponseDto loginUser(LoginRequestDto loginRequestDto) {

        Users userToLog = findUserByEmail(BasicEncrypt.encrypt(loginRequestDto.getEmail()));

        if(!userToLog.getActive())
            throw new NotActiveAccountException();
        if(userToLog.getBanned())
            throw new BannedAccountException();
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), userToLog.getPassword()))
            throw new PasswordsDoNotMatchException();

        try {
            AuthenticationCredentialsDto authCredentials = authenticationUtil.createCredentials(userToLog, null);

            return new LoginResponseWithHttpResponseDto(authCredentials.getAccessToken(), authCredentials.getRefreshToken(), authCredentials.getCsrfToken(), userToLog.getUserRole());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new CannotResponseToRequestException();
        }
    }

    @Override
    public ResponseEntity<?> loginWithFacebook(String storeId, LoginWithFacebookRequestDto loginWithFacebookRequestDto) {
        String getTokenUrl = "https://graph.facebook.com/v7.0/oauth/access_token?client_id="+ fbAppId +"&redirect_uri="+ loginWithFacebookRequestDto.getUrl() + "&client_secret="+ fbSecret +"&code="+loginWithFacebookRequestDto.getFbcode();

        FacebookLoginRequestDto accessToken=webClient.build()
                .get()
                .uri(getTokenUrl)
                .retrieve()
                .bodyToMono(FacebookLoginRequestDto.class)
                .block();

        if(accessToken == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        byte[] secretKey = fbSecret.getBytes();
        String proof = Hashing.hmacSha256(secretKey)
                .hashString(accessToken.getAccess_token(), StandardCharsets.UTF_8)
                .toString();

        String getFullName = "https://graph.facebook.com/me?fields=name&access_token="+accessToken.getAccess_token()+"&appsecret_proof=" +proof;
        FacebookProfileInfoDto info = webClient.build()
                .get()
                .uri(getFullName)
                .retrieve()
                .bodyToMono(FacebookProfileInfoDto.class)
                .block();

        if(info == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        if(hasUserAlreadyCommented(BasicEncrypt.encrypt(info.getName()), storeId) == true) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new LoginWithFacebookResponseDto(info.getName()));
    }

    @Override
    public void registerUser(RegisterRequestDto registerRequestDto) throws UserAlreadyRegisteredException, IOException, MessagingException {

        if(registerRequestDto.getPassword().length() < 8)
            throw new PasswordTooShortException();

        if(!registerRequestDto.getHasCheckedTerms())
            throw new HasNotAcceptedTermsException();

        if(!registerRequestDto.getConfirmationPassword().equals(registerRequestDto.getPassword()))
            throw new PasswordsDoNotMatchException();

        Users userToRegister = userMapper.convertRegisterRequestDtoToUser(registerRequestDto);
        byte[] encryptedEmail = BasicEncrypt.encrypt(registerRequestDto.getEmail());

        try {
            isPasswordOK(registerRequestDto.getPassword());
        } catch (PasswordTooShortException e) {
            throw new PasswordTooShortException();
        } catch (BreachedPasswordException e) {
            throw new BreachedPasswordException();
        } catch (PasswordTooWeakException e) {
            throw new PasswordTooWeakException();
        }

        if(encryptedEmail == null)
            throw new CannotResponseToRequestException();

        try {
            userToRegister
                    .setEmail(encryptedEmail)
                    .setUserRole(UserRoles.ROLE_USER.name())
                    .setPassword(passwordEncoder.encode(userToRegister.getPassword()))
                    .setActive(false)
                    .setBanned(false)
                    .setBalance(3000L)
                    .setSmallTokens(0)
                    .setMediumTokens(0)
                    .setLargeTokens(0)
                    .setUnlimitedTokens(0)
                    .setNoOfShopsAllowed(5)
                    .setNoOfShopsInAccount(0)
                    .setHasAddedBalance(false);
            saveUserWithoutReturning(userToRegister);

            MyUserDetails myUserDetails = new MyUserDetails(userToRegister);

            String refreshTokenId = UUIDGeneratorUtil.generateUniqueKeysWithUUIDAndMessageDigest();
            String refreshToken = refreshTokenUtil.generateRefreshToken(refreshTokenId, 60*24*1000);
            String token = accessTokenUtil.generateAccessToken(myUserDetails, 60*24*7, refreshToken);

            String frontendUrlEnvVar = System.getenv("SPRING_APP_FRONTEND_1") == null ? System.getenv("SPRING_APP_FRONTEND_2") : System.getenv("SPRING_APP_FRONTEND_1");
	        String frontendUrl = (frontendUrlEnvVar == null) ? "https://produsesiservicii.ro" : frontendUrlEnvVar;
            String endpoint = frontendUrl+"/confirmEmail?code="+token;
            emailUtil.sendConfirmationForRegistrationEmail(registerRequestDto.getEmail(), endpoint, "inregistrarea pe platforma Produse&Servicii.ro");
        } catch (UserAlreadyRegisteredException e) {
            throw new UserAlreadyRegisteredException();
        } catch (IOException e) {
            deleteUser(userToRegister);
            throw new IOException();
        } catch (MessagingException e) {
            deleteUser(userToRegister);
            System.out.println(e.getMessage());
            throw new MessagingException();
        } catch (NoSuchAlgorithmException e) {
            deleteUser(userToRegister);
            throw new CannotResponseToRequestException();
        }
    }

    @Override
    public Cookie logoutUser(HttpServletRequest httpServletRequest) {
        String authToDelete = getCookieByName(httpServletRequest, ACCESS_TOKEN);
        deleteAuthenticationByAccessToken(authToDelete);

        return deleteCookie(ACCESS_TOKEN);
    }

    @Override
    public AuthenticationCredentialsDto checkIdentity(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getCookieByName(request, ACCESS_TOKEN);
        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }
        
        return authenticationUtil.createCredentials(findUserById(userId), accessToken);
    }

    @Override
    public Cookie deleteCookie(String cookieName) {
        boolean isSecure = true;
        
	String frontendUrlEnvVar = System.getenv("SPRING_APP_FRONTEND_1") == null ? System.getenv("SPRING_APP_FRONTEND_2"): System.getenv("SPRING_APP_FRONTEND_1");
	if(frontendUrlEnvVar == null)
            isSecure = false;

        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(isSecure);

        return cookie;
    }

    @Override
    public String getCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(name))
                return cookie.getValue();
        }
        return null;
    }

    @Override
    public boolean hasUserAlreadyCommented(byte[] userName, String storeId) {
        Shops shop = findShopById(storeId);
        return existsCommentByShopAndUsername(shop, userName);
    }

    @Override
    public boolean isAccessTokenExpired(String token) {
        return accessTokenUtil.isTokenExpired(token);
    }

    @Override
    public String getIdFromAccessToken(String token) {
        return accessTokenUtil.extractId(token);
    }

    @Override
    public void saveUserWithoutReturning(Users user) throws UserAlreadyRegisteredException {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyRegisteredException();
        }
    }

    @Override
    public Users saveUserAndReturn(Users user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyRegisteredException();
        }
    }

    @Override
    public boolean isPasswordOK(String password) {
        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure(password);

        if (strength.getScore() < 3) {
            throw new PasswordTooWeakException();
        }

        PasswordPolicy policy = new PasswordPolicy(BreachDatabase.haveIBeenPwned(5), 8, 64);
        switch(policy.check(password)) {
            case TOO_SHORT:
                throw new PasswordTooShortException();
            case BREACHED:
                throw new BreachedPasswordException();
            default:
                return true;
        }
    }

    @Override
    public void deleteUser(Users user) {
        userRepository.delete(user);
    }

    @Override
    public void updateUserWithoutReturning(Users user) {
        userRepository.save(user);
    }

    @Override
    public Users findUserById(String id) throws UserNotRegisteredException {
        return userRepository.findUsersById(id).orElseThrow(UserNotRegisteredException::new);
    }

    @Override
    public Users findUserByEmail(byte[] email) throws UserNotRegisteredException {
        return userRepository.findUsersByEmail(email).orElseThrow(UserNotRegisteredException::new);
    }

    @Override
    public void saveAuthenticationDetailsWithoutReturning(Authentication auth) {
        authenticationRepository.save(auth);
    }

    @Override
    public void deleteAuthenticationByAccessToken(String accessToken) throws WrongCredentialsForRequestException {
        authenticationRepository.deleteByAccessToken(accessToken);
    }

    @Override
    public void deleteAuthenticationByDateBefore(Date date) {
        authenticationRepository.deleteAllByDateBefore(date);
    }

    @Override
    public Shops findShopById(String shopId) {
        return shopRepository.findById(shopId).orElseThrow(CannotFindShopException::new);
    }

    @Override
    public Boolean existsCommentByShopAndUsername(Shops shop, byte[] username) {
        return commentsRepository.existsByShopAndEmail(shop, username);
    }
}
