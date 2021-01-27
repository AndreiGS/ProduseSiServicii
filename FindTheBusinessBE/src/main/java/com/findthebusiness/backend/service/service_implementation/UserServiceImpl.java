package com.findthebusiness.backend.service.service_implementation;

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import com.findthebusiness.backend.dto.authentication.AuthenticationCredentialsDto;
import com.findthebusiness.backend.dto.categories.CategoriesDto;
import com.findthebusiness.backend.dto.shops.ShopCardDto;
import com.findthebusiness.backend.dto.subcategories.SubcategoriesDto;
import com.findthebusiness.backend.dto.users.*;
import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Subcategories;
import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.exception.*;
import com.findthebusiness.backend.mapper.mapper_implementation.ShopMapperImpl;
import com.findthebusiness.backend.repository.CategoryRepository;
import com.findthebusiness.backend.repository.ShopRepository;
import com.findthebusiness.backend.repository.SubcategoriesRepository;
import com.findthebusiness.backend.repository.UserRepository;
import com.findthebusiness.backend.security.utils.AccessTokenUtil;
import com.findthebusiness.backend.security.utils.AuthenticationUtil;
import com.findthebusiness.backend.security.utils.ChangeInfoTokenUtil;
import com.findthebusiness.backend.security.utils.ChangePasswordTokenUtil;
import com.findthebusiness.backend.service.service_repository.UserService;
import com.findthebusiness.backend.utils.BasicEncrypt;
import com.findthebusiness.backend.utils.EmailUtil;
import com.findthebusiness.backend.utils.enums.ShopSizesEnum;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final SubcategoriesRepository subcategoriesRepository;
    private final CategoryRepository categoriesRepository;

    private final ShopMapperImpl shopMapper;

    private final AccessTokenUtil accessTokenUtil;
    private final AuthenticationUtil authenticationUtil;
    private final ChangeInfoTokenUtil changeInfoTokenUtil;
    private final ChangePasswordTokenUtil changePasswordTokenUtil;

    private final PasswordEncoder passwordEncoder;

    private final EmailUtil emailUtil;

    public UserServiceImpl(ShopRepository shopRepository, UserRepository userRepository,
            SubcategoriesRepository subcategoriesRepository, CategoryRepository categoriesRepository, 
            ShopMapperImpl shopMapper, AccessTokenUtil accessTokenUtil,
            AuthenticationUtil authenticationUtil, ChangeInfoTokenUtil changeInfoTokenUtil,
            ChangePasswordTokenUtil changePasswordTokenUtil, PasswordEncoder passwordEncoder, EmailUtil emailUtil) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
        this.subcategoriesRepository = subcategoriesRepository;
        this.categoriesRepository = categoriesRepository;
        this.shopMapper = shopMapper;
        this.accessTokenUtil = accessTokenUtil;
        this.authenticationUtil = authenticationUtil;
        this.changeInfoTokenUtil = changeInfoTokenUtil;
        this.changePasswordTokenUtil = changePasswordTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailUtil = emailUtil;
    }

    // CONTROLLER METHODS
    @Override
    public ProfileInfoDtoWithAccessToken getUserProfileInfo(HttpServletRequest httpServletRequest) {
        // GET ACCESS-TOKEN FROM REQUEST
        String accessToken = getAccessTokenFromRequest(httpServletRequest);
        if (accessToken == null)
            throw new WrongCredentialsForRequestException();

        // GET USER ID FROM TOKEN
        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }

        // GET USER BY ID
        Users user = findUserById(userId);

        // GET SHOPS BY USER
        List<ShopCardDto> shops = getShopsForResponse(findShopsByUser(user));

        // GENERATE CREDENTIALS
        try {
            AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
            return new ProfileInfoDtoWithAccessToken(new ProfileInfoDto(BasicEncrypt.decrypt(user.getEmail()),
                    user.getName(), user.getBalance(), shops, findAllSubcategoriesDto(), findAllCategoriesDto(),
                    user.getSmallTokens(), user.getMediumTokens(), user.getLargeTokens(), user.getUnlimitedTokens(),
                    user.getNoOfShopsAllowed(), auth.getRefreshToken(), auth.getCsrfToken()), auth.getAccessToken());

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new CannotResponseToRequestException();
        }
    }

    @Override
    public ChangeInfoResponseDtoWithAccessToken changeInfo(ChangeInfoRequestDto changeInfoRequestDto,
            HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String accessToken = getAccessTokenFromRequest(request);
        String userId = null;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }
        if (userId == null) {
            throw new WrongCredentialsForRequestException();
        }

        String newEmail = changeInfoRequestDto.getNewEmail();
        String newName = changeInfoRequestDto.getNewName();

        String token = changeInfoTokenUtil.generateChangeInfoToken(userId, BasicEncrypt.encrypt(newEmail), newName,
                60 * 24 * 1000);

        String frontendUrlEnvVar = System.getenv("SPRING_APP_FRONTEND_1") == null
                ? System.getenv("SPRING_APP_FRONTEND_2")
                : System.getenv("SPRING_APP_FRONTEND_1");
        String frontendUrl = (frontendUrlEnvVar == null) ? "https://produsesiservicii.ro" : frontendUrlEnvVar;
        String endpoint = frontendUrl + "/changeInfo?code=" + token;

        try {
            emailUtil.sendConfirmationForRegistrationEmail(newEmail, endpoint, "schimbarea informatiilor profilului");

            Users user = findUserById(userId);
            AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);

            return new ChangeInfoResponseDtoWithAccessToken(
                    new ChangeInfoResponseDto(auth.getCsrfToken(), auth.getRefreshToken()), auth.getAccessToken());

        } catch (IOException e) {
            throw new IOException();
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new MessagingException();
        } catch (NoSuchAlgorithmException e) {
            throw new CannotResponseToRequestException();
        }
    }

    @Override
    public void saveNewInfo(String token, HttpServletRequest request, HttpServletResponse response) {
        if (changeInfoTokenUtil.isTokenExpired(token))
            throw new ChangeInfoTokenExpiredException();

        String userId = changeInfoTokenUtil.extractId(token);
        Claims claims = changeInfoTokenUtil.extractAllClaims(token);

        String newEmail = claims.get("newEmail", String.class);
        String newName = claims.get("newName", String.class);

        Users userToModify = findUserById(userId);
        userToModify.setName(newName);
        userToModify.setEmail(Base64.getDecoder().decode(newEmail));

        saveUserWithoutReturning(userToModify);
    }

    // Send email to user in order to confirm their action
    @Override
    public PasswordChangingResponseDtoWithAccessToken changePassword(HttpServletRequest request,
            HttpServletResponse response) throws IOException, MessagingException {
        String accessToken = getAccessTokenFromRequest(request);
        String userId = null;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }
        if (userId == null) {
            throw new WrongCredentialsForRequestException();
        }

        String token = changePasswordTokenUtil.generateChangePasswordToken(userId, 60 * 24 * 7);

        String frontendUrlEnvVar = System.getenv("SPRING_APP_FRONTEND_1") == null
                ? System.getenv("SPRING_APP_FRONTEND_2")
                : System.getenv("SPRING_APP_FRONTEND_1");
        String frontendUrl = (frontendUrlEnvVar == null) ? "https://produsesiservicii.ro" : frontendUrlEnvVar;
        String endpoint = frontendUrl + "/changePassword?code=" + token;

        try {
            Users user = findUserById(userId);

            emailUtil.sendConfirmationForRegistrationEmail(BasicEncrypt.decrypt(user.getEmail()), endpoint,
                    "schimbarea parolei contului");
            AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);

            return new PasswordChangingResponseDtoWithAccessToken(
                    new PasswordChangingResponseDto(auth.getCsrfToken(), auth.getRefreshToken()),
                    auth.getAccessToken());

        } catch (IOException e) {
            throw new IOException();
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            throw new MessagingException();
        } catch (NoSuchAlgorithmException e) {
            throw new CannotResponseToRequestException();
        }
    }

    @Override
    public CheckIfCanAddItemResponseDtoWithAccessToken checkIfCanAddItem(String shopId, HttpServletRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Shops shop = isOwner(shopId, request);

        if (shop == null)
            throw new NotTheOwnerException();

        if (shop.getActualSize() + 1 > shop.getMaximumSize())
            throw new CannotAddItemException();

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(shop.getUser(),
                getAccessTokenFromRequest(request));

        return new CheckIfCanAddItemResponseDtoWithAccessToken(auth.getAccessToken(),
                new CheckIfCanAddItemResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public Shops isOwner(String shopId, HttpServletRequest request) {
        String accessToken = getAccessTokenFromRequest(request);
        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }

        Shops shop = findShopById(shopId);
        if (shop == null)
            throw new CannotFindShopException();

        if (shop.getUser().getId().equals(userId))
            return shop;
        return null;
    }

    @Override
    public void saveNewPassword(String token, PasswordChangingRequestDto passwordChangingRequestDto,
            HttpServletRequest request, HttpServletResponse response) {
        if (changeInfoTokenUtil.isTokenExpired(token))
            throw new ChangePasswordTokenExpiredException();
        if (passwordChangingRequestDto.getPassword().length() < 8)
            throw new PasswordTooShortException();

        try {
            isPasswordOK(passwordChangingRequestDto.getPassword());
        } catch (PasswordTooShortException e) {
            throw new PasswordTooShortException();
        } catch (BreachedPasswordException e) {
            throw new BreachedPasswordException();
        } catch (PasswordTooWeakException e) {
            throw new PasswordTooWeakException();
        }

        String userId = changeInfoTokenUtil.extractId(token);
        String newPassword = passwordChangingRequestDto.getPassword();

        Users user = findUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));

        saveUserWithoutReturning(user);
    }

    @Override
    public ChargeResponseDtoWithAccessToken buyCredit(ChargeRequestDto chargeRequestDto, HttpServletRequest request) {
        try {
            String accessToken = getAccessTokenFromRequest(request);
            String userId;
            try {
                userId = accessTokenUtil.extractId(accessToken);
            } catch (ExpiredJwtException e) {
                userId = e.getClaims().getSubject();
            }

            Users user = findUserById(userId);

            Charge charge = charge(chargeRequestDto, user);
            if (!charge.getStatus().equals("succeeded"))
                throw new CannotResponseToRequestException();

            AuthenticationCredentialsDto auth = addUserCredit(charge.getAmount() / 100, accessToken, user, request);

            long amount = charge.getAmount();
            if (user.getHasAddedBalance() == false)
                amount = amount + ((10 * amount) / 100);

            return new ChargeResponseDtoWithAccessToken(auth.getAccessToken(),
                    new ChargeResponseDto(auth.getCsrfToken(), auth.getRefreshToken(), amount));
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new CannotResponseToRequestException();
        }
    }

    @Override
    public Charge charge(ChargeRequestDto chargeRequest, Users user) throws AuthenticationException,
            InvalidRequestException, APIConnectionException, CardException, APIException {
        Stripe.apiKey = System.getenv("SPRING_APP_STRIPE");

        String frontendUrl = (System.getenv("SPRING_APP_FRONTEND_1") != null) ? System.getenv("SPRING_APP_FRONTEND_1")
                : "localhost:8081";

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount() * 100);
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("source", chargeRequest.getTkn());
        chargeParams.put("description",
                "Adaugare credite in valoare de " + chargeRequest.getAmount() + " RON pe website-ul: " + frontendUrl);
        chargeParams.put("receipt_email", BasicEncrypt.decrypt(user.getEmail()));
        return Charge.create(chargeParams);
    }

    @Override
    public AuthenticationCredentialsDto addUserCredit(Long amount, String accessToken, Users user,
            HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        if (user.getHasAddedBalance() == false) {
            amount = amount + ((10 * amount) / 100);
            user.setHasAddedBalance(true);
        }

        user.setBalance(user.getBalance() + amount);

        saveUserWithoutReturning(user);

        return authenticationUtil.createCredentials(user, accessToken);
    }

    @Override
    public void deleteProfile(HttpServletRequest request) {
        String accessToken = getAccessTokenFromRequest(request);
        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }

        deleteUserById(userId);
    }

    @Override
    public AddShopTokenResponseDtoWithAccessToken addShopToken(String type, HttpServletRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);

        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }

        Users user = findUserById(userId);
        Long userBalance = user.getBalance();
        Long balanceSubtracted = 0L;
        switch (type) {
            case "small":
                if (userBalance < ShopSizesEnum.SMALL.price)
                    throw new NotEnoughCreditException();
                if(user.getSmallTokens() + 1 > 100)
                    throw new TooManyShopTokensException();
                balanceSubtracted = ShopSizesEnum.SMALL.price;
                user.setSmallTokens(user.getSmallTokens() + 1);
                user.setBalance(userBalance - ShopSizesEnum.SMALL.price);
                break;
            case "medium":
                if (userBalance < ShopSizesEnum.MEDIUM.price)
                    throw new NotEnoughCreditException();
                if(user.getMediumTokens() + 1 > 100)
                    throw new TooManyShopTokensException();
                balanceSubtracted = ShopSizesEnum.MEDIUM.price;
                user.setMediumTokens(user.getMediumTokens() + 1);
                user.setBalance(userBalance - ShopSizesEnum.MEDIUM.price);
                break;
            case "large":
                if (userBalance < ShopSizesEnum.LARGE.price)
                    throw new NotEnoughCreditException();
                if(user.getLargeTokens() + 1 > 100)
                    throw new TooManyShopTokensException();
                balanceSubtracted = ShopSizesEnum.LARGE.price;
                user.setLargeTokens(user.getLargeTokens() + 1);
                user.setBalance(userBalance - ShopSizesEnum.LARGE.price);
                break;
            case "unlimited":
                if (userBalance < ShopSizesEnum.UNLIMITED.price)
                    throw new NotEnoughCreditException();
                if(user.getUnlimitedTokens() + 1 > 100)
                    throw new TooManyShopTokensException();
                balanceSubtracted = ShopSizesEnum.UNLIMITED.price;
                user.setUnlimitedTokens(user.getUnlimitedTokens() + 1);
                user.setBalance(userBalance - ShopSizesEnum.UNLIMITED.price);
                break;
        }

        saveUserWithoutReturning(user);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new AddShopTokenResponseDtoWithAccessToken(auth.getAccessToken(),
                new AddShopTokenResponseDto(balanceSubtracted, auth.getRefreshToken(), auth.getCsrfToken()));
    }

    // CUSTOM METHODS
    public boolean isPasswordOK(String password) {
        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure(password);

        if (strength.getScore() < 3) {
            throw new PasswordTooWeakException();
        }

        PasswordPolicy policy = new PasswordPolicy(BreachDatabase.haveIBeenPwned(5), 8, 64);
        switch (policy.check(password)) {
            case TOO_SHORT:
                throw new PasswordTooShortException();
            case BREACHED:
                throw new BreachedPasswordException();
            default:
                return true;
        }
    }

    @Override
    public String getAccessTokenFromRequest(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS_TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public List<ShopCardDto> getShopsForResponse(List<Shops> shops) {
        return shopMapper.convertShopsToShopCardDto(shops);
    }

    // JPA METHODS
    @Override
    public Users findUserById(String id) throws UserNotRegisteredException {
        return userRepository.findUsersById(id).orElseThrow(UserNotRegisteredException::new);
    }

    @Override
    public List<Shops> findShopsByUser(Users user) {
        return shopRepository.findAllByUserOrderByRefreshedAtDesc(user).orElseGet(ArrayList::new);
    }

    @Override
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void saveUserWithoutReturning(Users user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyRegisteredException();
        }
    }

    @Override
    public List<SubcategoriesDto> findAllSubcategoriesDto() {
        return shopMapper.convertSubcategoriesToSubcategoriesDtoList(findAllSubcategories());
    }

    @Override
    public List<Subcategories> findAllSubcategories() {
        return subcategoriesRepository.findAll();
    }

    @Override
    public Shops findShopById(String shopId) {
        return shopRepository.findById(shopId).orElseThrow(CannotFindShopException::new);
    }

    @Override
    public List<CategoriesDto> findAllCategoriesDto() {
        return shopMapper.convertCategoriesToCategoriesDtoList(findAllCategories());
    }

    @Override
    public List<Categories> findAllCategories() {
        return categoriesRepository.findAll();
    }
}
