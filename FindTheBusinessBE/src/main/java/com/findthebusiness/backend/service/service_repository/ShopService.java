package com.findthebusiness.backend.service.service_repository;

import com.findthebusiness.backend.dto.shops.*;
import com.findthebusiness.backend.entity.*;
import com.findthebusiness.backend.utils.enums.ShopSizesEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ShopService {

    //CONTROLLER METHODS
    ResponseEntity<PromotedShopsPageDto> getShopsForPage(Integer page);
    ResponseEntity<StoreFrontRequestDto> getShopDetailsById(String shopId);
    ResponseEntity<ReloadRatingAndPriceResponseDto> getRatingAndPrice(String shopId);
    RefreshShopResponseWithAccessToken refreshShop(String id, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    PromoteShopResponseWithAccessToken promoteShop(String id, String type, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    SaveShopResponseDtoWithAccessToken uploadShop(String shopId, SaveShopRequestDto saveShopRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception;
    DeleteShopResponseDtoWithAccessToken deleteShop(String shopId, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    CheckIfShopOwnerReponseDtoWithAccessToken checkIfShopOwner(String shopId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    ChangeContactDataResponseDtoWithAccessToken changeContactData(String shopId, ChangeContactDataRequestDto changeContactDataRequestDto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    ChangePublishedResponseDtoWithAccessToken changePublished(String shopId, Boolean wantsToContinue, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken changeHasAutomaticTokenRefresh(String shopId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    ChangeStorefrontImageResponseDtoWithAccessToken changeStorefrontImage(String shopId, String imageType, ChangeStorefrontImageRequestDto changeStorefrontImageRequestDto, HttpServletRequest request) throws IOException, NoSuchAlgorithmException;

    void setShopsNotPromotedAfterExpiring();
    void unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshEnabled();
    void unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshDisabled();
    //CUSTOM METHODS
    String getAccessTokenFromRequest(HttpServletRequest request);
    Shops saveNewShop(Users user, SaveShopRequestDto saveShopRequestDto) throws Exception;
    Shops updateShop(String shopId, SaveShopRequestDto saveShopRequestDto, HttpServletRequest request) throws Exception;

    void setShopsNotPromotedInHomeAfterExpiring();
    void setShopsNotPromotedInSearchesAfterExpiring();
    void promoteShopInHome(Shops shop, Users user);
    void promoteShopInSearches(Shops shop, Users user);
    List<Subcategories> findSubcategoriesForShop(List<String> subcategories);
    Subcategories findSubcategoryByName(String subcategory);
    ShopSizesEnum getShopSize(Integer size);
    void subtractTokenIfPossible(Shops shop, Boolean shouldThrowError);
    Boolean hasNecessaryToken(Users user, String tokenName);
    String savePhoto(String base64Photo, String photoUtility) throws IOException, NoSuchAlgorithmException;
    Integer getTotalNumberOfPages();
    String uploadFileToS3Bucket(final String bucketName, final File file, final String uniqueFileName);
    File convertBlobToFile(String base64Photo, String uniqueFileName) throws IOException;
    void deleteFile(final String keyName);
    Shops isOwner(String shopId, String accessToken, HttpServletRequest request);

    //JPA METHODS
    Shops findShopById(String shopId);
    void saveShopWithoutReturning(Shops shop);
    Shops saveShop(Shops shop);
    void saveAllShops(List<Shops> shops);
    void subtractPromotedShopToPages();
    void addPromotedShopToPages();
    Pages findPages();
    void savePages(Pages pages);
    List<Shops> findAllByIsPromotedInHome(Boolean isPromoted, Pageable pageable);
    List<Shops> findAllPromotedInHomeShops();
    List<Shops> findAllPromotedInSearchesShops();
    List<Shops> findAllPublishedShopsAndAutomaticTokenRefreshTrue();
    List<Shops> findAllPublishedShopsAndAutomaticTokenRefreshFalse();
    void deleteShopById(String id);
    void deleteShop(Shops shops);

    Users findUserById(String userId);
    void saveUser(Users users);

    List<Tabs> findTabsByShop(Shops shop);
}
