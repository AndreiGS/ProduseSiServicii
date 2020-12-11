package com.findthebusiness.backend.service.service_implementation;

import com.findthebusiness.backend.dto.authentication.AuthenticationCredentialsDto;
import com.findthebusiness.backend.dto.tabs.*;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;
import com.findthebusiness.backend.exception.*;
import com.findthebusiness.backend.mapper.mapper_repository.TabMapper;
import com.findthebusiness.backend.repository.ItemsRepository;
import com.findthebusiness.backend.repository.ShopRepository;
import com.findthebusiness.backend.repository.TabRepository;
import com.findthebusiness.backend.security.utils.AccessTokenUtil;
import com.findthebusiness.backend.security.utils.AuthenticationUtil;
import com.findthebusiness.backend.service.service_repository.TabService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TabServiceImpl implements TabService {

    private final String ACCESS_TOKEN = "ACCESS-TOKEN";

    private final AccessTokenUtil accessTokenUtil;
    private final AuthenticationUtil authenticationUtil;

    private final ShopRepository shopRepository;
    private final ItemsRepository itemsRepository;
    private final TabRepository tabRepository;

    private final TabMapper tabMapper;

    public TabServiceImpl(AccessTokenUtil accessTokenUtil, AuthenticationUtil authenticationUtil, ShopRepository shopRepository, ItemsRepository itemsRepository, TabRepository tabRepository, TabMapper tabMapper) {
        this.accessTokenUtil = accessTokenUtil;
        this.authenticationUtil = authenticationUtil;
        this.shopRepository = shopRepository;
        this.itemsRepository = itemsRepository;
        this.tabRepository = tabRepository;
        this.tabMapper = tabMapper;
    }

    @Override
    public PostTabResponseDtoWithAccessToken postTab(String shopId, String tabId, PostTabRequestDto postTabRequestDto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(postTabRequestDto.getName().length() > 20) {
            throw new NameTooLongException();
        }

        Shops shops = isOwner(shopId, request);

        if(shops == null)
            throw new NotTheOwnerException();

        List<Tabs> shopTabs = findTabsByShop(shops);

        Tabs oldTab = null;

        for (Tabs tab : shopTabs) {
            if(tab.getName().equals(postTabRequestDto.getName())) {
                throw new TabAlreadyExistsException();
            }
            if(tab.getId().equals(tabId)) {
                oldTab = tab;
            }
        }

        Tabs tabToSave = null;
        if(tabId.equals("not-set")) {
            tabToSave = tabMapper.convertPostTabRequestDtoToTabs(postTabRequestDto);
            tabToSave.setShops(shops);
        } else {
            if(oldTab != null) {
                tabToSave = oldTab;
                tabToSave.setName(postTabRequestDto.getName());
            }
        }

        if(tabToSave == null)
            throw new CannotSaveTabException();

        Tabs newTab = saveTabWithReturning(tabToSave);

        if(oldTab != null) {
            List<Items> items = findItemsByShopAndTab(shops, oldTab);
            for(Items item : items) {
                item.setTabs(tabToSave);
                saveItemWithoutReturning(item);
            }
        }

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(shops.getUser(), getAccessTokenFromRequest(request));
        return new PostTabResponseDtoWithAccessToken(auth.getAccessToken(), new PostTabResponseDto(new TabResponseDto(newTab.getId(), newTab.getName()), auth.getRefreshToken(), auth.getCsrfToken()));
    }

    @Override
    public DeleteTabResponseDtoWithAccessToken deleteTab(String shopId, String tabId, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Shops shops = isOwner(shopId, request);
        if(shops == null)
            throw new NotTheOwnerException();

        Tabs tabToDelete = findTabByTabId(tabId);
        List<Items> itemsToDeleteTab = findItemsByShopAndTab(shops, tabToDelete);

        deleteTabFromItems(itemsToDeleteTab);
        deleteTab(tabToDelete);

        AuthenticationCredentialsDto auth = authenticationUtil.createCredentials(shops.getUser(), getAccessTokenFromRequest(request));
        return new DeleteTabResponseDtoWithAccessToken(auth.getAccessToken(), new DeleteTabResponseDto(auth.getRefreshToken(), auth.getCsrfToken()));
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
        if(shop.getUser().getId().equals(userId))
            return shop;
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
    public Tabs saveTabWithReturning(Tabs tabs) {
        return tabRepository.save(tabs);
    }

    @Override
    public List<Tabs> findTabsByShop(Shops shop) {
        return tabRepository.findTabsByShops(shop).orElseGet(ArrayList::new);
    }

    @Override
    public void deleteTabById(String tabId) {
        tabRepository.deleteById(tabId);
    }

    @Override
    public Tabs findTabByTabId(String tabId) {
        Optional<Tabs> tab = tabRepository.findById(tabId);
        return tab.isPresent() ? tab.get() : null;
    }

    @Override
    public void deleteTab(Tabs tab) {
        tabRepository.delete(tab);
    }

    @Override
    public List<Items> findItemsByShopAndTab(Shops shops, Tabs tabs) {
        return itemsRepository.findItemsByShopAndTabs(shops, tabs).orElseGet(ArrayList::new);
    }

    @Override
    public void saveItemWithoutReturning(Items items) {
        itemsRepository.save(items);
    }

    @Override
    public void deleteTabFromItems(List<Items> items) {
        for (Items itemToDeleteTab : items) {
            itemToDeleteTab.setTabs(null);
            saveItemWithoutReturning(itemToDeleteTab);
        }
    }
}
