package com.findthebusiness.backend.service.service_implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.findthebusiness.backend.dto.authentication.AuthenticationCredentialsDto;
import com.findthebusiness.backend.dto.shops.*;
import com.findthebusiness.backend.dto.tabs.TabResponseDto;
import com.findthebusiness.backend.entity.*;
import com.findthebusiness.backend.exception.*;
import com.findthebusiness.backend.mapper.mapper_implementation.ShopMapperImpl;
import com.findthebusiness.backend.repository.*;
import com.findthebusiness.backend.security.utils.AccessTokenUtil;
import com.findthebusiness.backend.security.utils.AuthenticationUtil;
import com.findthebusiness.backend.service.service_repository.ShopService;
import com.findthebusiness.backend.utils.UUIDGeneratorUtil;
import com.findthebusiness.backend.utils.enums.ShopActionsPriceEnum;
import com.findthebusiness.backend.utils.enums.ShopAttributesMaxSizeEnum;
import com.findthebusiness.backend.utils.enums.ShopSizesEnum;
import com.findthebusiness.backend.utils.enums.ShopPromotionTypesEnum;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ShopServiceImpl implements ShopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopServiceImpl.class);

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";

    private final String SMALL_SHOP_IMAGE = "SMALL_SHOP_IMAGE";
    private final String LARGE_SHOP_IMAGE = "LARGE_SHOP_IMAGE";

    private final ShopRepository shopRepository;
    private final ShopMapperImpl shopMapperImpl;
    private final PagesRepository pagesRepository;
    private final UserRepository userRepository;
    private final SubcategoriesRepository subcategoriesRepository;
    private final TabRepository tabRepository;
    private final ItemsServiceImpl itemsService;
    private final AccessTokenUtil accessTokenUtil;
    private final AuthenticationUtil authenticationUtil;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public ShopServiceImpl(ShopRepository shopRepository, ShopMapperImpl shopMapperImpl, PagesRepository pagesRepository, UserRepository userRepository, SubcategoriesRepository subcategoriesRepository, TabRepository tabRepository, ItemsServiceImpl itemsService, AccessTokenUtil accessTokenUtil, AuthenticationUtil authenticationUtil, AmazonS3 amazonS3) {
        this.shopRepository = shopRepository;
        this.shopMapperImpl = shopMapperImpl;
        this.pagesRepository = pagesRepository;
        this.userRepository = userRepository;
        this.subcategoriesRepository = subcategoriesRepository;
        this.tabRepository = tabRepository;
        this.itemsService = itemsService;
        this.accessTokenUtil = accessTokenUtil;
        this.authenticationUtil = authenticationUtil;
        this.amazonS3 = amazonS3;
    }

    @Override
    public Integer getTotalNumberOfPages(){
        int noOfPromotedShops = findPages().getPromotedShopsNo();

        double pagesDouble = (double) noOfPromotedShops/24;
        int pagesInt = noOfPromotedShops/24;
        if(pagesDouble  == pagesInt)
            return noOfPromotedShops/24;

        return  noOfPromotedShops/24+1;
        //return pagesRepository.findAll().get(0).getPromotedShopsNo()/24+1;
    }

    //CONTROLLER METHODS
    @Override
    public ResponseEntity<PromotedShopsPageDto> getShopsForPage(Integer page) {
        int nrOfItemsToRequest = 24;
        Pageable pageable = PageRequest.of(
                                                page,
                                                nrOfItemsToRequest,
                                                Sort.by(
                                                        Sort.Order.desc("refreshedAt"),
                                                        Sort.Order.desc("boughtAt"),
                                                        Sort.Order.desc("actualSize")
                                                )
                                            );
        List<Shops> shopsList = findAllByIsPromotedInHome(true, pageable);
        int noOfElementsRetrieved = shopsList.size();

        if(noOfElementsRetrieved >= 24 || page > 0) {
            PromotedShopsPageDto promotedShopsPageDto = new PromotedShopsPageDto();
            promotedShopsPageDto.setShops(shopMapperImpl.convertShopsToShopCardDto(shopsList))
                                .setTotalPages(getTotalNumberOfPages());
            return ResponseEntity.ok(promotedShopsPageDto);
        }

        pageable = PageRequest.of(
                                    page,
                                    nrOfItemsToRequest-noOfElementsRetrieved,
                                    Sort.by(
                                            Sort.Order.desc("refreshedAt"),
                                            Sort.Order.desc("boughtAt"),
                                            Sort.Order.desc("actualSize")
                                    )
                                );

        List<Shops> notPromotedShops = findAllByIsPromotedInHome(false, pageable);
        int noOfNotPromotedShops = notPromotedShops.size();

        if(noOfNotPromotedShops == 0 && noOfElementsRetrieved == 0){
            return ResponseEntity.notFound().build();
        }

        if(noOfNotPromotedShops == 0) {
            PromotedShopsPageDto promotedShopsPageDto = new PromotedShopsPageDto();
            promotedShopsPageDto.setShops(shopMapperImpl.convertShopsToShopCardDto(shopsList))
                                .setTotalPages(1);
            return ResponseEntity.ok(promotedShopsPageDto);
        }

        List<Shops> shopsToRetrieve = new ArrayList<>();
        shopsToRetrieve.addAll(shopsList);
        shopsToRetrieve.addAll(notPromotedShops);

        noOfElementsRetrieved+=noOfNotPromotedShops;

        if(noOfElementsRetrieved <= 24) {
            PromotedShopsPageDto promotedShopsPageDto = new PromotedShopsPageDto();
            promotedShopsPageDto.setShops(shopMapperImpl.convertShopsToShopCardDto(shopsToRetrieve))
                                .setTotalPages(1);
            return ResponseEntity.ok(promotedShopsPageDto);
        }

        PromotedShopsPageDto promotedShopsPageDto = new PromotedShopsPageDto();
        promotedShopsPageDto.setShops(shopMapperImpl.convertShopsToShopCardDto(shopsToRetrieve.subList(0, 24)))
                            .setTotalPages(1);

        return ResponseEntity.ok(promotedShopsPageDto);
    }

    @Override
    public ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken changeHasAutomaticTokenRefresh(String shopId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);

        Shops shop = isOwner(shopId, accessToken, request);
        Users user = shop.getUser();

        shop.setHasAutomaticTokenRefresh(!shop.getHasAutomaticTokenRefresh());
        saveShop(shop);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken(auth.getAccessToken(), new ChangeHasAutomaticTokenRefreshResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public void unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshEnabled() {
        List<Shops> shops = findAllPublishedShopsAndAutomaticTokenRefreshTrue();

        for(Shops shop : shops) {
            Date shopBoughtDate = shop.getBoughtAt();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(shopBoughtDate);
            calendar.add(Calendar.MONTH, 1);

            Date shopExpireDate = calendar.getTime();
            Date todayDate = new Date();

            if(todayDate.before(shopExpireDate)) {
                continue;
            }

            subtractTokenIfPossible(shop, false);
        }
    }

    @Override
    public void unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshDisabled() {
        List<Shops> shops = findAllPublishedShopsAndAutomaticTokenRefreshFalse();

        for(Shops shop : shops) {
            Date shopBoughtDate = shop.getBoughtAt();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(shopBoughtDate);
            calendar.add(Calendar.MONTH, 1);

            Date shopExpireDate = calendar.getTime();
            Date todayDate = new Date();

            if(todayDate.before(shopExpireDate)) {
                continue;
            }

            shop.setPublished(false);
            saveShop(shop);
        }
    }

    @Override
    public void setShopsNotPromotedAfterExpiring() {
        setShopsNotPromotedInHomeAfterExpiring();
        setShopsNotPromotedInSearchesAfterExpiring();
    }

    @Override
    public ResponseEntity<StoreFrontRequestDto> getShopDetailsById(String shopId) {
        try {
            Shops shop = findShopById(shopId);
            StoreFrontRequestDto storeFrontRequestDto = shopMapperImpl.convertShopsToStoreFrontRequestDto(shop);

            if(shop.getRatingNumber() != null && shop.getRatingNumber() != 0)
                storeFrontRequestDto.setRating(shop.getRating() / shop.getRatingNumber());
            else
                storeFrontRequestDto.setRating(0.0);

            if(shop.getPriceNumber() != null && shop.getPriceNumber() != 0) {
                double price = (double)shop.getPrice() / shop.getPriceNumber();
                storeFrontRequestDto.setPrice(price);
            }
            else
                storeFrontRequestDto.setPrice(0.0);

            Set<TabResponseDto> tabs = new TreeSet<>();
            List<Tabs> foundTabs = findTabsByShop(shop);
            for(Tabs tab : foundTabs) {
                tabs.add(new TabResponseDto(tab.getId(), tab.getName()));
            }
            storeFrontRequestDto.setTabs(tabs);

            return ResponseEntity.ok(storeFrontRequestDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<ReloadRatingAndPriceResponseDto> getRatingAndPrice(String shopId) {
        try {
            Shops shop = findShopById(shopId);
            ReloadRatingAndPriceResponseDto reloadRatingAndPriceResponseDto = new ReloadRatingAndPriceResponseDto();
            if(shop.getRatingNumber() != null && shop.getRatingNumber() != 0)
                reloadRatingAndPriceResponseDto.setRating(shop.getRating() / shop.getRatingNumber());
            else
                reloadRatingAndPriceResponseDto.setRating(0.0);

            if(shop.getPriceNumber() != null && shop.getPriceNumber() != 0) {
                double price = (double)shop.getPrice() / shop.getPriceNumber();
                reloadRatingAndPriceResponseDto.setPrice(price);
            }
            else
                reloadRatingAndPriceResponseDto.setPrice(0.0);

            return ResponseEntity.ok(reloadRatingAndPriceResponseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public RefreshShopResponseWithAccessToken refreshShop(String id, HttpServletRequest request, HttpServletResponse response) throws NotTheOwnerException, NotEnoughCreditException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);

        Shops shop = isOwner(id, accessToken, request);
        Users user = shop.getUser();


        long userNewBalance = user.getBalance() - ShopActionsPriceEnum.REFRESH_SHOP_PRICE.price;
        if(userNewBalance < 0)
            throw new NotEnoughCreditException();
        user.setBalance(userNewBalance);
        saveUser(user);

        shop.setRefreshedAt(new Date());
        saveShop(shop);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new RefreshShopResponseWithAccessToken(auth.getAccessToken(), new RefreshShopResponse(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public PromoteShopResponseWithAccessToken promoteShop(String id, String type, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);

        Shops shop = isOwner(id, accessToken, request);
        Users user = shop.getUser();

        if(type.equals(ShopPromotionTypesEnum.HOME.name())) {
            promoteShopInHome(shop, user);
        } else if(type.equals(ShopPromotionTypesEnum.SEARCHES.name())) {
            promoteShopInSearches(shop, user);
        }

        /*String accessToken = getAccessTokenFromRequest(request);

        Shops shop = isOwner(id, accessToken, request);
        String previousType = shop.getType();
        Users user = shop.getUser();

        long userNewBalance = user.getBalance() - ShopActionsPriceEnum.PROMOTE_SHOP_PROMOTE.price;
        if(userNewBalance < 0)
            throw new NotEnoughCreditException();

        shop.setType(ShopTypesEnum.PROMOTED.name());
        shop.setPromotedDate(new Date());

        if(shop.getPromotedDays() == null)
            shop.setPromotedDays(0);

        if(!previousType.equals(ShopTypesEnum.PROMOTED.name()))
            addPromotedShopToPages();

        shop.setPromotedDays(shop.getPromotedDays()+30);
        saveShop(shop);

        user.setBalance(userNewBalance);
        saveUser(user);*/

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new PromoteShopResponseWithAccessToken(auth.getAccessToken(), new PromoteShopResponse(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public ChangePublishedResponseDtoWithAccessToken changePublished(String shopId, Boolean wantsToContinue, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);

        Shops shop = isOwner(shopId, accessToken, request);
        Users user = shop.getUser();

        ShopSizesEnum shopSize = getShopSize(shop.getMaximumSize());

        if(!shopSize.name().equals("FREE") && shop.getPublished() == false) {
            Date shopBoughtDate = shop.getBoughtAt();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(shopBoughtDate);
            calendar.add(Calendar.MONTH, 1);

            Date shopExpireDate = calendar.getTime();

            boolean isExpired = shopExpireDate.before(new Date());

            if(isExpired == true && (wantsToContinue == false || wantsToContinue == null)) {
                throw new CannotChangePublishException();
            } else if(isExpired == true && wantsToContinue == true) {
                subtractTokenIfPossible(shop, true);
            }
        }

        shop.setPublished(!shop.getPublished());
        saveShop(shop);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new ChangePublishedResponseDtoWithAccessToken(auth.getAccessToken(), new ChangePublishedResponseDto(auth.getRefreshToken(), auth.getCsrfToken(), shop.getPublished()));
    }

    @Override
    public SaveShopResponseDtoWithAccessToken uploadShop(String shopId, SaveShopRequestDto saveShopRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(saveShopRequestDto.getName().length() > ShopAttributesMaxSizeEnum.TITLE_SIZE.size) {
            throw new NameTooLongException();
        }

        if(saveShopRequestDto.getDescription().length() > ShopAttributesMaxSizeEnum.DESCRIPTION_SIZE.size) {
            throw new DescriptionTooLongException();
        }

        String accessToken = getAccessTokenFromRequest(request);
        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }

        Users user = findUserById(userId);

        Shops newShop;
        if(shopId.contains("not-set")) {
            if(user.getNoOfShopsInAccount() + 1 > user.getNoOfShopsAllowed()) {
                throw new ShopsLimitExceeded();
            }
            if(saveShopRequestDto.getBase64SmallImage() == null)
                throw new PhotoRequiredException();

            newShop = saveNewShop(user, saveShopRequestDto);
        } else {
            newShop = updateShop(shopId, saveShopRequestDto, request);
        }

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new SaveShopResponseDtoWithAccessToken(new SaveShopResponseDto(auth.getRefreshToken(), auth.getCsrfToken(), newShop.getId(), newShop.getSmallPhoto()), auth.getAccessToken());
    }

    @Override
    public Shops saveNewShop(Users user, SaveShopRequestDto saveShopRequestDto) throws Exception {
        if(!hasNecessaryToken(user, saveShopRequestDto.getShopSize())) {
            throw new NotEnoughTokensException();
        }

        Shops newShop = shopMapperImpl.convertSaveShopRequestDtoToShop(saveShopRequestDto, user);
        String photoUrl = savePhoto(saveShopRequestDto.getBase64SmallImage(), SMALL_SHOP_IMAGE);

        if(photoUrl == null)
            throw new Exception();

        newShop.setPromotedInSearches(false)
                .setPromotedInHome(false)
                .setBoughtAt(new Date())
                .setRefreshedAt(new Date())
                .setPublished(false)
                .setActualSize(0)
                .setLargePhoto(null)
                .setSmallPhoto(photoUrl)
                .setMaximumSize(ShopSizesEnum.valueOf(saveShopRequestDto.getShopSize()).size)
                .setHasAutomaticTokenRefresh(true);

        subtractTokenIfPossible(newShop, true);

        user.setNoOfShopsInAccount(user.getNoOfShopsInAccount() + 1);

        newShop = saveShop(newShop);

        saveUser(user);

        return newShop;
    }

    @Override
    public Shops updateShop(String shopId, SaveShopRequestDto saveShopRequestDto, HttpServletRequest request) throws Exception {
        String accessToken = getAccessTokenFromRequest(request);

        Shops updatedShop = isOwner(shopId, accessToken, request);
        Users user = updatedShop.getUser();
        Integer newSize = ShopSizesEnum.valueOf(saveShopRequestDto.getShopSize()).size;

        if(!saveShopRequestDto.getShopSize().equals(getShopSize(updatedShop.getMaximumSize()).name())) {
            if(!hasNecessaryToken(user, saveShopRequestDto.getShopSize())) {
                throw new NotEnoughTokensException();
            } else {
                updatedShop.setMaximumSize(newSize);
                subtractTokenIfPossible(updatedShop, true);
            }
        }

        if(updatedShop.getActualSize() > newSize) {
            throw new CannotChangeSizeException();
        }

        updatedShop.setPhone(saveShopRequestDto.getPhoto());
        updatedShop.setName(saveShopRequestDto.getName());
        updatedShop.setDescription(saveShopRequestDto.getDescription());
        updatedShop.setSubcategories(findSubcategoriesForShop(saveShopRequestDto.getSubcategories()));
        updatedShop.setMaximumSize(newSize);

        if(saveShopRequestDto.getBase64SmallImage() != null) {
            String photoUrl = savePhoto(saveShopRequestDto.getBase64SmallImage(), SMALL_SHOP_IMAGE);

            if(photoUrl == null)
                throw new Exception();

            if(updatedShop.getSmallPhoto() != null) {
                try {
                    String[] oldPhotoName = updatedShop.getSmallPhoto().split("/");
                    deleteFile(oldPhotoName[oldPhotoName.length-1]);
                } catch (Exception e ){

                }
            }

            updatedShop.setSmallPhoto(photoUrl);
        }

        return saveShop(updatedShop);
    }

    @Override
    public DeleteShopResponseDtoWithAccessToken deleteShop(String shopId, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);
        Shops shops = isOwner(shopId, accessToken, request);
        Users user = shops.getUser();

        if(shops.getSmallPhoto() != null) {
            try {
                String[] oldPhotoName = shops.getSmallPhoto().split("/");

                deleteFile(oldPhotoName[oldPhotoName.length-1]);
            } catch (Exception e) {

            }
        }

        if(shops.getLargePhoto() != null) {
            try {
                String[] oldPhotoName = shops.getLargePhoto().split("/");

                deleteFile(oldPhotoName[oldPhotoName.length-1]);
            } catch (Exception e) {

            }
        }

        deleteShop(shops);

        user.setNoOfShopsInAccount(user.getNoOfShopsInAccount() - 1);
        saveUser(user);

        if(shops.getPromotedInHome() == true) {
            try {
                subtractPromotedShopToPages();
            } catch (Exception e) {

            }
        }

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new DeleteShopResponseDtoWithAccessToken(auth.getAccessToken(), new DeleteShopResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public CheckIfShopOwnerReponseDtoWithAccessToken checkIfShopOwner(String shopId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);

        Shops shop = isOwner(shopId, accessToken, request);

        Users user = shop.getUser();
        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new CheckIfShopOwnerReponseDtoWithAccessToken(auth.getAccessToken(), new CheckIfShopOwnerResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public ChangeStorefrontImageResponseDtoWithAccessToken changeStorefrontImage(String shopId, String imageType, ChangeStorefrontImageRequestDto changeStorefrontImageRequestDto, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);
        Shops shop = isOwner(shopId, accessToken, request);
        Users user = shop.getUser();

        String largePhoto = shop.getLargePhoto();
        String smallPhoto = shop.getSmallPhoto();

        String imageUrl = null;
        if(changeStorefrontImageRequestDto.getNewImage() != null) {
            if(imageType.equals("LARGE")) {
                imageUrl = savePhoto(changeStorefrontImageRequestDto.getNewImage(), LARGE_SHOP_IMAGE);
                shop.setLargePhoto(imageUrl);
            } else {
                imageUrl = savePhoto(changeStorefrontImageRequestDto.getNewImage(), SMALL_SHOP_IMAGE);
                shop.setSmallPhoto(imageUrl);
            }
        }

        if(shop.getLargePhoto() != null) {
            try {
                String[] oldPhotoName;
                if(imageType.equals("LARGE")) {
                    oldPhotoName = largePhoto.split("/");
                } else {
                    oldPhotoName = smallPhoto.split("/");
                }
                deleteFile(oldPhotoName[oldPhotoName.length-1]);

            } catch (Exception e) {

            }
        }

        shop.setName(changeStorefrontImageRequestDto.getName());
        shop.setDescription(changeStorefrontImageRequestDto.getDescription());
        shop.setSchedule(changeStorefrontImageRequestDto.getSchedule());
        saveShop(shop);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new ChangeStorefrontImageResponseDtoWithAccessToken(auth.getAccessToken(), new ChangeStorefrontImageResponseDto(auth.getRefreshToken(), auth.getCsrfToken(), imageUrl));
    }

    @Override
    public ChangeContactDataResponseDtoWithAccessToken changeContactData(String shopId, ChangeContactDataRequestDto changeContactDataRequestDto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String accessToken = getAccessTokenFromRequest(request);
        Shops shop = isOwner(shopId, accessToken, request);

        shop.setWebsiteLink(changeContactDataRequestDto.getWebsiteLink())
            .setPhone(changeContactDataRequestDto.getPhone())
            .setAddress(changeContactDataRequestDto.getAddress())
            .setEmail(changeContactDataRequestDto.getEmail())
            .setCounty(changeContactDataRequestDto.getCounty());

        saveShop(shop);

        Users user = shop.getUser();
        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(user, accessToken);
        return new ChangeContactDataResponseDtoWithAccessToken(auth.getAccessToken(), new ChangeContactDataResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public ShopSizesEnum getShopSize(Integer size) {
        if(size.equals(ShopSizesEnum.FREE.size))
            return ShopSizesEnum.FREE;
        if(size.equals(ShopSizesEnum.SMALL.size))
            return ShopSizesEnum.SMALL;
        if(size.equals(ShopSizesEnum.MEDIUM.size))
            return ShopSizesEnum.MEDIUM;
        if(size.equals(ShopSizesEnum.LARGE.size))
            return ShopSizesEnum.LARGE;
        return ShopSizesEnum.UNLIMITED;
    }

    @Override
    public Shops isOwner(String shopId, String accessToken, HttpServletRequest request) {
        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }

        Shops shops = findShopById(shopId);
        Users user = shops.getUser();

        if(!user.getId().equals(userId))
            throw new NotTheOwnerException();

        return shops;
    }

    @Override
    @Async
    public String savePhoto(String base64Photo, String photoUtility) throws IOException, NoSuchAlgorithmException {
        File file=null;
        try {
            final String uniqueFileName = photoUtility + "_" + UUIDGeneratorUtil.generateUniqueKeysWithUUIDAndMessageDigest() + ".png";
            file = convertBlobToFile(base64Photo, uniqueFileName);
            String imageUrl = uploadFileToS3Bucket(bucketName, file, LocalDateTime.now() +  "_" + uniqueFileName);

            LOGGER.info("File upload is completed.");
            file.delete();  // To remove the file locally created in the project folder.

            return imageUrl;
        } catch (final AmazonServiceException ex) {
            file.delete();
            LOGGER.info("File upload has failed.");
            LOGGER.error("Error= {} while uploading file.", ex.getMessage());
            return null;
        }
    }

    @Override
    public File convertBlobToFile(String base64Photo, String uniqueFileName) throws IOException {
        String[] canvasBase64Photo = base64Photo.split(",");

        byte[] blobImageFile = Base64.getDecoder().decode(canvasBase64Photo[1]);
        final File file = new File(uniqueFileName);

        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(blobImageFile);
        } catch (final IOException ex) {
            LOGGER.error("Error converting blob to file= " + ex.getMessage());
        }
        return file;
    }

    @Override
    public String uploadFileToS3Bucket(final String bucketName, final File file, final String uniqueFileName) {
        LOGGER.info("Uploading file with name= " + uniqueFileName);
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file).withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);

        return "https://"+bucketName+".s3.amazonaws.com/"+uniqueFileName;
    }

    @Override
    @Async
    public void deleteFile(final String keyName) {
        LOGGER.info("Deleting file with name= " + keyName);
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, keyName);
        amazonS3.deleteObject(deleteObjectRequest);
        LOGGER.info("File deleted successfully.");
    }

    @Override
    public void subtractTokenIfPossible(Shops shop, Boolean shouldThrowError) {
        ShopSizesEnum shopSize = getShopSize(shop.getMaximumSize());

        if(shopSize.name().equals("FREE"))
            return;

        Users shopOwner = shop.getUser();

        if(shopSize.name().equals("SMALL")) {
            if(shopOwner.getSmallTokens() <= 0) {
                if(shouldThrowError == true)
                    throw new NotEnoughTokensException();

                shop.setPublished(false);
                saveShop(shop);
                return;
            }
            shopOwner.setSmallTokens(shopOwner.getSmallTokens() - 1);
            shop.setBoughtAt(new Date());
            saveShop(shop);
            saveUser(shopOwner);
            return;
        }

        if(shopSize.name().equals("MEDIUM")) {
            if(shopOwner.getMediumTokens() <= 0) {
                if(shouldThrowError == true)
                    throw new NotEnoughTokensException();
                shop.setPublished(false);
                saveShop(shop);
                return;
            }
            shopOwner.setMediumTokens(shopOwner.getMediumTokens() - 1);
            shop.setBoughtAt(new Date());
            saveShop(shop);
            saveUser(shopOwner);
            return;
        }

        if(shopSize.name().equals("LARGE")) {
            if(shopOwner.getLargeTokens() <= 0) {
                if(shouldThrowError == true)
                    throw new NotEnoughTokensException();
                shop.setPublished(false);
                saveShop(shop);
                return;
            }
            shopOwner.setLargeTokens(shopOwner.getLargeTokens() - 1);
            shop.setBoughtAt(new Date());
            saveShop(shop);
            saveUser(shopOwner);
            return;
        }

        if(shopSize.name().equals("UNLIMITED")) {
            if(shopOwner.getUnlimitedTokens() <= 0) {
                if(shouldThrowError == true)
                    throw new NotEnoughTokensException();
                shop.setPublished(false);
                saveShop(shop);
                return;
            }
            shopOwner.setUnlimitedTokens(shopOwner.getUnlimitedTokens() - 1);
            shop.setBoughtAt(new Date());
            saveShop(shop);
            saveUser(shopOwner);
            return;
        }
    }

    @Override
    public Boolean hasNecessaryToken(Users user, String tokenName) {
        if(tokenName.equals("SMALL")) {
            return user.getSmallTokens() > 0;
        }
        if(tokenName.equals("MEDIUM")) {
            return user.getMediumTokens() > 0;
        }
        if(tokenName.equals("LARGE")) {
            return user.getLargeTokens() > 0;
        }
        if(tokenName.equals("UNLIMITED")) {
            return user.getUnlimitedTokens() > 0;
        }
        return true;
    }

    @Override
    public void setShopsNotPromotedInHomeAfterExpiring() {
        List<Shops> shops = findAllPromotedInHomeShops();
        Calendar calendar = Calendar.getInstance();
        for(Shops shop : shops) {
            calendar.setTime(shop.getPromotedDateInHome());
            calendar.add(Calendar.DAY_OF_MONTH, shop.getPromotedDaysInHome());
            Date expiryDate = calendar.getTime();
            Date currentDate = new Date();
            if(currentDate.after(expiryDate)) {
                shop.setPromotedDateInHome(null);
                shop.setPromotedInHome(false);
                shop.setPromotedDaysInHome(null);
            }
        }
        saveAllShops(shops);
    }

    @Override
    public void setShopsNotPromotedInSearchesAfterExpiring() {
        List<Shops> shops = findAllPromotedInSearchesShops();
        Calendar calendar = Calendar.getInstance();
        for(Shops shop : shops) {
            calendar.setTime(shop.getPromotedDateInSearches());
            calendar.add(Calendar.DAY_OF_MONTH, shop.getPromotedDaysInSearches());
            Date expiryDate = calendar.getTime();
            Date currentDate = new Date();
            if(currentDate.after(expiryDate)) {
                shop.setPromotedDateInSearches(null);
                shop.setPromotedInSearches(false);
                shop.setPromotedDaysInSearches(null);
            }
        }
        saveAllShops(shops);
    }

    @Override
    public List<Subcategories> findSubcategoriesForShop(List<String> subcategories) {
        List<Subcategories> subcategoriesList = new ArrayList<>();
        for(String subcategory : subcategories) {
            if(!subcategory.equals("none")){
                Subcategories subcategoryListItem = findSubcategoryByName(subcategory);
                if(subcategoryListItem == null)
                    continue;
                subcategoriesList.add(subcategoryListItem);
            }
        }

        return subcategoriesList;
    }

    @Override
    public void promoteShopInHome(Shops shop, Users user) {
        Boolean isPromotedInHome = shop.getPromotedInHome();

        long userNewBalance = user.getBalance() - ShopActionsPriceEnum.PROMOTE_SHOP_PROMOTE_HOME.price;
        if(userNewBalance < 0)
            throw new NotEnoughCreditException();

        shop.setPromotedInHome(true);
        if(!isPromotedInHome) {
            shop.setPromotedDateInHome(new Date());
        }

        if(shop.getPromotedDaysInHome() == null) {
            shop.setPromotedDaysInHome(0);
            addPromotedShopToPages();
        }

        shop.setPromotedDaysInHome(shop.getPromotedDaysInHome()+30);
        saveShop(shop);

        user.setBalance(userNewBalance);
        saveUser(user);
    }

    @Override
    public void promoteShopInSearches(Shops shop, Users user) {
        Boolean isPromotedInSearches = shop.getPromotedInSearches();

        long userNewBalance = user.getBalance() - ShopActionsPriceEnum.PROMOTE_SHOP_PROMOTE_SEARCHES.price;
        if(userNewBalance < 0)
            throw new NotEnoughCreditException();

        shop.setPromotedInSearches(true);
        if(!isPromotedInSearches) {
            shop.setPromotedDateInSearches(new Date());
        }

        if(shop.getPromotedDaysInSearches() == null) {
            shop.setPromotedDaysInSearches(0);
        }

        shop.setPromotedDaysInSearches(shop.getPromotedDaysInSearches()+30);
        saveShop(shop);

        user.setBalance(userNewBalance);
        saveUser(user);
    }

    @Override
    public Subcategories findSubcategoryByName(String subcategory) {
        Optional<Subcategories> subcategoryFound = subcategoriesRepository.findByName(subcategory);

        if(subcategoryFound.isPresent())
            return subcategoryFound.get();
        else
            return null;
    }

    @Override
    public String getAccessTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(ACCESS_TOKEN))
                return cookie.getValue();
        }
        return null;
    }

    @Override
    public Shops findShopById(String shopId) {
        return shopRepository.findById(shopId).orElseThrow(CannotFindShopException::new);
    }

    @Override
    public void saveShopWithoutReturning(Shops shop) {
        shopRepository.save(shop);
    }

    @Override
    public Shops saveShop(Shops shop) {
        return shopRepository.save(shop);
    }

    @Override
    public List<Shops> findAllByIsPromotedInHome(Boolean isPromoted, Pageable pageable) {
        return shopRepository.findAllByIsPromotedInHomeAndIsPublished(isPromoted, true, pageable).orElseGet(ArrayList::new);
    }

    @Override
    public List<Shops> findAllPromotedInHomeShops() {
        return shopRepository.findAllByIsPromotedInHome(true).orElseGet(ArrayList::new);
    }

    @Override
    public List<Shops> findAllPromotedInSearchesShops() {
        return shopRepository.findAllByIsPromotedInSearches(true).orElseGet(ArrayList::new);
    }

    @Override
    public void deleteShopById(String id) {
        shopRepository.deleteById(id);
    }

    @Override
    public void deleteShop(Shops shops) {
        shopRepository.delete(shops);
    }

    @Override
    public Users findUserById(String userId) {
        return userRepository.findUsersById(userId).orElseThrow(UserNotRegisteredException::new);
    }

    @Override
    public void saveUser(Users users) {
        userRepository.save(users);
    }

    @Override
    public List<Tabs> findTabsByShop(Shops shop) {
        return tabRepository.findTabsByShops(shop).orElseGet(ArrayList::new);
    }

    @Override
    public void saveAllShops(List<Shops> shops) {
        shopRepository.saveAll(shops);
    }

    @Override
    public void subtractPromotedShopToPages() {
        Pages pages = findPages();
        pages.setPromotedShopsNo(pages.getPromotedShopsNo() - 1);
        savePages(pages);
    }

    @Override
    public void addPromotedShopToPages() {
        Pages pages = findPages();
        pages.setPromotedShopsNo(pages.getPromotedShopsNo() + 1);
        savePages(pages);
    }

    @Override
    public Pages findPages() {
        return pagesRepository.findAll().get(0);
    }

    @Override
    public void savePages(Pages pages) {
        pagesRepository.save(pages);
    }

    @Override
    public List<Shops> findAllPublishedShopsAndAutomaticTokenRefreshTrue() {
        return shopRepository.findAllByIsPublishedAndHasAutomaticTokenRefresh(true, true).orElseGet(ArrayList::new);
    }

    @Override
    public List<Shops> findAllPublishedShopsAndAutomaticTokenRefreshFalse() {
        return shopRepository.findAllByIsPublishedAndHasAutomaticTokenRefresh(true, false).orElseGet(ArrayList::new);
    }
}
