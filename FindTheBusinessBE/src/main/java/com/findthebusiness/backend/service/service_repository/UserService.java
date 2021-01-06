package com.findthebusiness.backend.service.service_repository;

import com.findthebusiness.backend.dto.authentication.AuthenticationCredentialsDto;
import com.findthebusiness.backend.dto.categories.CategoriesDto;
import com.findthebusiness.backend.dto.shops.ShopCardDto;
import com.findthebusiness.backend.dto.subcategories.SubcategoriesDto;
import com.findthebusiness.backend.dto.users.*;
import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Subcategories;
import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.exception.UserNotRegisteredException;
import com.stripe.exception.*;
import com.stripe.model.Charge;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public interface UserService {

    //CONTROLLER METHODS
    ProfileInfoDtoWithAccessToken getUserProfileInfo(HttpServletRequest httpServletRequest);
    ChangeInfoResponseDtoWithAccessToken changeInfo(ChangeInfoRequestDto changeInfoRequestDto, HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException;
    void saveNewInfo(String token, HttpServletRequest request, HttpServletResponse response);
    PasswordChangingResponseDtoWithAccessToken changePassword(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException;
    void saveNewPassword(String code, PasswordChangingRequestDto passwordChangingRequestDto, HttpServletRequest request, HttpServletResponse response);
    ChargeResponseDtoWithAccessToken buyCredit(ChargeRequestDto chargeRequestDto, HttpServletRequest request);
    void deleteProfile(HttpServletRequest request);
    CheckIfCanAddItemResponseDtoWithAccessToken checkIfCanAddItem(String shopId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    AddShopTokenResponseDtoWithAccessToken addShopToken(String type, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    //CUSTOM METHODS
    String getAccessTokenFromRequest(HttpServletRequest httpServletRequest);
    List<ShopCardDto> getShopsForResponse(List<Shops> shops);
    Charge charge(ChargeRequestDto chargeRequest, Users user) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException;
    AuthenticationCredentialsDto addUserCredit(Long amount, String accessToken, Users user, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    Shops isOwner(String shopId, HttpServletRequest request);
    boolean isPasswordOK(String password);

    //JPA METHODS
    Users findUserById(String id) throws UserNotRegisteredException;
    List<Shops> findShopsByUser(Users user);
    void deleteUserById(String userId);
    void saveUserWithoutReturning(Users user);
    List<SubcategoriesDto> findAllSubcategoriesDto();
    List<CategoriesDto> findAllCategoriesDto();
    List<Subcategories> findAllSubcategories();
    List<Categories> findAllCategories();
    Shops findShopById(String shopId);

}
