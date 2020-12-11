package com.findthebusiness.backend.service.service_repository;

import com.findthebusiness.backend.dto.items.*;
import com.findthebusiness.backend.dto.tabs.DeleteTabResponseDtoWithAccessToken;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ItemsService {

    //CONTROLLER METHODS
    ResponseEntity<?> getItems(String shopId, GetItemsRequestDto getItemsRequestDto);
    DeleteItemResponseDtoWithAccessToken deleteItem(String shopId, String itemId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    AddItemResponseDtoWithAccessToken addItem(String shopId, String itemId, AddItemRequestDto addItemRequestDto, HttpServletRequest request) throws IOException, NoSuchAlgorithmException;

    //CUSTOM METHODS
    String getAccessTokenFromRequest(HttpServletRequest request);
    Shops isOwner(String shopId, HttpServletRequest request);
    List<Items> getItemsByTabAndShop(Shops shop, Tabs tab);
    List<Items> getAllItemsByShop(String shopId);
    Items saveNewItem(AddItemRequestDto addItemRequestDto, Shops shops) throws IOException, NoSuchAlgorithmException;
    Items updateItem(String itemId, AddItemRequestDto addItemRequestDto, Shops shops) throws IOException, NoSuchAlgorithmException;
    ItemResponseDto convertItemsToItemResponseDto(Items items);
    Items convertAddItemRequestDtoToItems(AddItemRequestDto items, Shops shops, Tabs tabs);
    List<Items> sortItems(List<Items> items,  List<String> itemsIds);
    String savePhoto(String base64Photo, String photoUtility) throws IOException, NoSuchAlgorithmException;
    File convertBlobToFile(String base64Photo, String uniqueFileName) throws IOException;
    String uploadFileToS3Bucket(final String bucketName, final File file, final String uniqueFileName);
    void deleteFile(final String keyName);

    //JPA METHODS
    List<Items> findItemsByShop(Shops shop);
    List<Items> findItemsByShopAndTab(Shops shop, Tabs tab);
    Shops findShopById(String shopId);
    Tabs findTabByNameAndShop(String tabName, Shops shop);
    Items findItemById(String id);
    void deleteItem(Items item);
    Items saveItem(Items items);
    void saveShop(Shops shops);
}
