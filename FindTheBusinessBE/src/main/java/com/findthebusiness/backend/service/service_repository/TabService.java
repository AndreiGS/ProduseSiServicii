package com.findthebusiness.backend.service.service_repository;

import com.findthebusiness.backend.dto.shops.DeleteShopResponseDtoWithAccessToken;
import com.findthebusiness.backend.dto.tabs.DeleteTabResponseDtoWithAccessToken;
import com.findthebusiness.backend.dto.tabs.PostTabRequestDto;
import com.findthebusiness.backend.dto.tabs.PostTabResponseDtoWithAccessToken;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface TabService {

    //CONTROLLER METHODS
    PostTabResponseDtoWithAccessToken postTab(String shopId, String tabId, PostTabRequestDto postTabRequestDto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    DeleteTabResponseDtoWithAccessToken deleteTab(String shopId, String tabId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    //CUSTOM METHODS
    String getAccessTokenFromRequest(HttpServletRequest request);
    Shops isOwner(String shopId, HttpServletRequest request);

    //JPA METHODS
    List<Tabs> findTabsByShop(Shops shop);
    Tabs saveTabWithReturning(Tabs tabs);
    void deleteTabById(String tabId);
    Tabs findTabByTabId(String tabId);
    void deleteTab(Tabs tab);

    Shops findShopById(String shopId);
    List<Items> findItemsByShopAndTab(Shops shops, Tabs tabs);
    void saveItemWithoutReturning(Items items);
    void deleteTabFromItems(List<Items> items);

}
