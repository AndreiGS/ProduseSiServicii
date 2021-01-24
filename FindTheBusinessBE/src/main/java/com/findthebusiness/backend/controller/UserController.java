package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.dto.shops.ShopCardDto;
import com.findthebusiness.backend.dto.users.*;
import com.findthebusiness.backend.exception.CannotAddItemException;
import com.findthebusiness.backend.exception.NotEnoughCreditException;
import com.findthebusiness.backend.exception.TooManyShopTokensException;
import com.findthebusiness.backend.service.service_implementation.UserServiceImpl;
import com.findthebusiness.backend.service.service_repository.UserService;
import com.sun.mail.iap.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/getUserProfileInfo")
    private ResponseEntity<ProfileInfoDto> getUserInfo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try{
            ProfileInfoDtoWithAccessToken profileInfoDtoWithAccessToken = userService.getUserProfileInfo(httpServletRequest);
            httpServletResponse.addCookie(profileInfoDtoWithAccessToken.getAccessToken());

            return ResponseEntity.ok(profileInfoDtoWithAccessToken.getProfileInfoDto());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteProfile")
    private ResponseEntity<?> deleteProfile(HttpServletRequest request, HttpServletResponse response) {
        try {
            userService.deleteProfile(request);

            Cookie accessToken = new Cookie("ACCESS-TOKEN", "");
            accessToken.setMaxAge(0);
            accessToken.setHttpOnly(true);

            boolean isSecure = true;
            String frontendUrlEnvVar = System.getenv("SPRING_APP_FRONTEND_1") == null ? System.getenv("SPRING_APP_FRONTEND_2") : System.getenv("SPRING_APP_FRONTEND_1");
            if(frontendUrlEnvVar == null)
                isSecure = false;

            accessToken.setSecure(isSecure);

            response.addCookie(accessToken);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sendEmailForChangingInformation")
    private ResponseEntity<?> sendEmailForChangingInformation(@RequestBody ChangeInfoRequestDto changeInfoRequestDto, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            ChangeInfoResponseDtoWithAccessToken changeInfoResponseDtoWithAccessToken = userService.changeInfo(changeInfoRequestDto, httpServletRequest, httpServletResponse);

            httpServletResponse.addCookie(changeInfoResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(changeInfoResponseDtoWithAccessToken.getChangeInfoResponseDto());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/changeInfo")
    private ResponseEntity<?> changeInfo(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
        try {
            userService.saveNewInfo(code, request, response);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/sendEmailForChangingPassword")
    private ResponseEntity<?> sendEmailForChangingPassword(HttpServletRequest request, HttpServletResponse response) {
        try {
            PasswordChangingResponseDtoWithAccessToken passwordChangingResponseDtoWithAccessToken = userService.changePassword(request, response);

            response.addCookie(passwordChangingResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(passwordChangingResponseDtoWithAccessToken.getPasswordChangingResponseDto());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/changePassword")
    private ResponseEntity<?> changePassword(@RequestParam("code") String code, @RequestBody PasswordChangingRequestDto passwordChangingRequestDto, HttpServletRequest request, HttpServletResponse response){
        userService.saveNewPassword(code, passwordChangingRequestDto, request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/charge")
    private ResponseEntity<?> charge(@RequestBody ChargeRequestDto chargeRequestDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            ChargeResponseDtoWithAccessToken chargeResponseDto = userService.buyCredit(chargeRequestDto, request);

            response.addCookie(chargeResponseDto.getAccessToken());
            return ResponseEntity.ok(chargeResponseDto.getChargeResponseDto());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/checkIfCanAddItem")
    private ResponseEntity<?> checkIfCanAddItem(@RequestParam("id") String shopId, HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckIfCanAddItemResponseDtoWithAccessToken checkIfCanAddItemResponseDtoWithAccessToken = userService.checkIfCanAddItem(shopId, request);

            response.addCookie(checkIfCanAddItemResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(checkIfCanAddItemResponseDtoWithAccessToken.getCheckIfCanAddItemResponseDto());
        } catch (CannotAddItemException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/addShopToken")
    private ResponseEntity<?> addShopToken(@RequestParam("type") String type, HttpServletRequest request, HttpServletResponse response) {
        try {
            AddShopTokenResponseDtoWithAccessToken addShopTokenResponseDtoWithAccessToken = userService.addShopToken(type, request);

            response.addCookie(addShopTokenResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(addShopTokenResponseDtoWithAccessToken.getAddShopTokenResponseDto());
        } catch (NotEnoughCreditException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (TooManyShopTokensException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
