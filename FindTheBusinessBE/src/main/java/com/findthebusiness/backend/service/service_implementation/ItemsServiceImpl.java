package com.findthebusiness.backend.service.service_implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.findthebusiness.backend.dto.authentication.AuthenticationCredentialsDto;
import com.findthebusiness.backend.dto.items.*;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;
import com.findthebusiness.backend.exception.*;
import com.findthebusiness.backend.mapper.mapper_implementation.ItemsMapperImpl;
import com.findthebusiness.backend.repository.ItemsRepository;
import com.findthebusiness.backend.repository.ShopRepository;
import com.findthebusiness.backend.repository.TabRepository;
import com.findthebusiness.backend.security.utils.AccessTokenUtil;
import com.findthebusiness.backend.security.utils.AuthenticationUtil;
import com.findthebusiness.backend.service.service_repository.ItemsService;
import com.findthebusiness.backend.utils.UUIDGeneratorUtil;
import com.findthebusiness.backend.utils.enums.ShopAttributesMaxSizeEnum;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ItemsServiceImpl implements ItemsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemsServiceImpl.class);

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";
    private final String ITEM_IMAGE = "ITEM_IMAGE";

    private final ItemsRepository itemsRepository;
    private final ShopRepository shopRepository;
    private final TabRepository tabRepository;

    private final ItemsMapperImpl itemsMapper;

    private final AccessTokenUtil accessTokenUtil;
    private final AuthenticationUtil authenticationUtil;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public ItemsServiceImpl(ItemsRepository itemsRepository, ShopRepository shopRepository, TabRepository tabRepository, ItemsMapperImpl itemsMapper, AccessTokenUtil accessTokenUtil, AuthenticationUtil authenticationUtil, AmazonS3 amazonS3) {
        this.itemsRepository = itemsRepository;
        this.shopRepository = shopRepository;
        this.tabRepository = tabRepository;
        this.itemsMapper = itemsMapper;
        this.accessTokenUtil = accessTokenUtil;
        this.authenticationUtil = authenticationUtil;
        this.amazonS3 = amazonS3;
    }

    @Override
    public ResponseEntity<?> getItems(String shopId, GetItemsRequestDto getItemsRequestDto) {
        try {
            List<Items> items;
            if(getItemsRequestDto.getTab().equals("all")) {
                items = getAllItemsByShop(shopId);
            } else {
                Shops shop = findShopById(shopId);
                Tabs tabs = findTabByNameAndShop(getItemsRequestDto.getTab(), shop);
                items = getItemsByTabAndShop(shop, tabs);
            }

            List<String> itemsIds = getItemsRequestDto.getFoundItemsIds();
            if(itemsIds != null && itemsIds.size() > 0) {
                items = sortItems(items, itemsIds);
            }

            List<GetItemsResponseDto> itemsDto = itemsMapper.convertItemsToGetItemsResponseDto(items);
            return ResponseEntity.ok(new ItemsByTabResponseDto(itemsDto));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public DeleteItemResponseDtoWithAccessToken deleteItem(String shopId, String itemId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Shops shop = isOwner(shopId, request);
        if(shop == null)
            throw new NotTheOwnerException();

        Items items = findItemById(itemId);

        if(items.getPhoto() != null) {
            try {
                String[] oldPhotoName = items.getPhoto().split("/");
                deleteFile(oldPhotoName[oldPhotoName.length-1]);
            } catch (Exception e) {

            }
        }

        deleteItem(items);

        shop.setActualSize(shop.getActualSize() - 1);
        saveShop(shop);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(shop.getUser(), getAccessTokenFromRequest(request));
        return new DeleteItemResponseDtoWithAccessToken(auth.getAccessToken(), new DeleteItemResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public AddItemResponseDtoWithAccessToken addItem(String shopId, String itemId, AddItemRequestDto addItemRequestDto, HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
        if(addItemRequestDto.getTitle().length() > ShopAttributesMaxSizeEnum.TITLE_SIZE.size) {
            throw new NameTooLongException();
        }

        if(addItemRequestDto.getDescription().length() > ShopAttributesMaxSizeEnum.DESCRIPTION_SIZE.size) {
            throw new DescriptionTooLongException();
        }

        if(addItemRequestDto.getNewImage() == null && itemId.equals("not-set"))
            throw new PhotoRequiredException();

        Shops shop = isOwner(shopId, request);
        if(shop == null)
            throw new NotTheOwnerException();

        Items newItem;
        if(itemId.equals("not-set")) {
            newItem = saveNewItem(addItemRequestDto, shop);
        } else {
            newItem = updateItem(itemId, addItemRequestDto, shop);
        }

        ItemResponseDto newItemDto = convertItemsToItemResponseDto(newItem);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(shop.getUser(), getAccessTokenFromRequest(request));
        return new AddItemResponseDtoWithAccessToken(auth.getAccessToken(), new AddItemResponseDto(auth.getRefreshToken(), auth.getCsrfToken(), newItemDto));
    }

    //CUSTOM METHODS
    @Override
    public List<Items> sortItems(List<Items> items, List<String> itemsIds) {
        int swapPos = 0;
        int itemIndex = 0;

        for(Items item : items) {
            for(String id : itemsIds) {
                if(!item.getId().equals(id)) {
                    continue;
                }

                if(swapPos == itemIndex)
                    continue;

                Collections.swap(items, swapPos, itemIndex);
                swapPos++;
            }
            itemIndex++;
        }

        return items;
    }

    @Override
    public Items convertAddItemRequestDtoToItems(AddItemRequestDto items, Shops shops, Tabs tabs) {
        return itemsMapper.convertAddItemRequestDtoToItems(items, shops, tabs);
    }

    @Override
    public Items saveNewItem(AddItemRequestDto addItemRequestDto, Shops shops) throws IOException, NoSuchAlgorithmException {
        Tabs tab = addItemRequestDto.getTabName().equals("none") ? null : findTabByNameAndShop(addItemRequestDto.getTabName(), shops);
        Items items = convertAddItemRequestDtoToItems(addItemRequestDto, shops, tab);

        String itemPhotoUrl = savePhoto(addItemRequestDto.getNewImage(), ITEM_IMAGE);

        if(items.getPhoto() != null) {
            try {
                String[] oldPhotoName = items.getPhoto().split("/");
                deleteFile(oldPhotoName[oldPhotoName.length-1]);
            } catch (Exception e) {

            }
        }

        items.setPhoto(itemPhotoUrl);

        Integer actualSize = shops.getActualSize();
        if(actualSize + 1 > shops.getMaximumSize())
            throw new CannotAddItemException();

        shops.setActualSize(actualSize + 1);
        saveShop(shops);

        return saveItem(items);
    }

    @Override
    public Items updateItem(String itemId, AddItemRequestDto addItemRequestDto, Shops shops) throws IOException, NoSuchAlgorithmException {
        Items items = findItemById(itemId);
        Tabs tab = addItemRequestDto.getTabName().equals("none") ? null : findTabByNameAndShop(addItemRequestDto.getTabName(), shops);

        items
                .setTitle(addItemRequestDto.getTitle())
                .setDescription(addItemRequestDto.getDescription())
                .setPrice(addItemRequestDto.getPrice())
                .setTabs(tab);

        String itemPhotoUrl;
        if(addItemRequestDto.getNewImage() != null) {
            itemPhotoUrl = savePhoto(addItemRequestDto.getNewImage(), ITEM_IMAGE);

            if(items.getPhoto() != null) {
                try {
                    String[] oldPhotoName = items.getPhoto().split("/");
                    deleteFile(oldPhotoName[oldPhotoName.length-1]);
                } catch (Exception e) {

                }
            }

            items.setPhoto(itemPhotoUrl);
        }

        return saveItem(items);
    }

    @Override
    public Items saveItem(Items items) {
        return itemsRepository.save(items);
    }

    @Override
    public ItemResponseDto convertItemsToItemResponseDto(Items items) {
        return itemsMapper.convertItemsToItemResponseDto(items);
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
    public Shops isOwner(String shopId, HttpServletRequest request) {
        String accessToken = getAccessTokenFromRequest(request);
        String userId;
        try {
            userId = accessTokenUtil.extractId(accessToken);
        } catch (ExpiredJwtException e) {
            userId = e.getClaims().getSubject();
        }

        Shops shop = findShopById(shopId);
        if(shop == null)
            throw new CannotFindShopException();

        if(shop.getUser().getId().equals(userId))
            return shop;
        return null;
    }

    @Override
    @Async
    public String savePhoto(String base64Photo, String photoUtility) throws IOException, NoSuchAlgorithmException {

        try {
            final String uniqueFileName = photoUtility + "_" + UUIDGeneratorUtil.generateUniqueKeysWithUUIDAndMessageDigest() + ".png";
            File file = convertBlobToFile(base64Photo, uniqueFileName);
            String imageUrl = uploadFileToS3Bucket(bucketName, file, LocalDateTime.now() +  "_" + uniqueFileName);

            LOGGER.info("File upload is completed.");
            file.delete();  // To remove the file locally created in the project folder.

            return imageUrl;
        } catch (final AmazonServiceException ex) {
            LOGGER.info("File upload is failed.");
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
    public List<Items> getItemsByTabAndShop(Shops shop, Tabs tab) {
        return findItemsByShopAndTab(shop, tab);
    }

    @Override
    public List<Items> getAllItemsByShop(String shopId) {
        Shops shop = findShopById(shopId);

        return findItemsByShop(shop);
    }

    @Override
    public List<Items> findItemsByShop(Shops shop) {
        return itemsRepository.findItemsByShop(shop).orElseGet(ArrayList::new);
    }

    @Override
    public List<Items> findItemsByShopAndTab(Shops shop, Tabs tab) {
        return itemsRepository.findItemsByShopAndTabs(shop, tab).orElseGet(ArrayList::new);
    }

    @Override
    public Shops findShopById(String shopId) {
        return shopRepository.findById(shopId).orElseThrow(CannotFindShopException::new);
    }

    @Override
    public Tabs findTabByNameAndShop(String tabName, Shops shop) {
        Optional<Tabs> tab = tabRepository.findTabsByNameAndShops(tabName, shop);

        return tab.isPresent() ? tab.get() : null;
    }

    @Override
    public Items findItemById(String id) {
        Optional<Items> item = itemsRepository.findItemsById(id);
        return item.isPresent() ? item.get() : null;
    }

    @Override
    public void deleteItem(Items item) {
        itemsRepository.delete(item);
    }

    @Override
    public void saveShop(Shops shops) {
        shopRepository.save(shops);
    }
}
