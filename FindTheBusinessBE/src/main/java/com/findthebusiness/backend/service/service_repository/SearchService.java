package com.findthebusiness.backend.service.service_repository;

import com.findthebusiness.backend.dto.items.GetItemsResponseDto;
import com.findthebusiness.backend.dto.items.ItemResponseDto;
import com.findthebusiness.backend.dto.search.*;
import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Subcategories;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

public interface SearchService {

    //CONTROLLER METHODS
    List<SearchShopResponseDto> search(String searchBarText, String category, String subcategory, Double rating, String county) throws Exception;
    CategoriesAndSubcategoriesDto getCategoriesAndSubcategories();
    List<GetSubcategoriesByCategoryRequestDto> getSubcategoriesByCategory(String categoryId);

    //CUSTOM METHODS
    List<SearchShopResponseDto> generateFromItemList(String searchBarText, String subcategory, String category, Double rating, String county);
    List<SearchShopResponseDto> generateFromShopTitleOrDescription(String searchBarText, String subcategory, String category, Double rating, String county);
    List<SearchShopResponseDto> generateFromShopDescription(List<Shops> foundShops, String searchBarText);

    /*int kmp(String input, String title);
    void computeLPSArray(String pat, int M, int lps[]);
    int lcs(char[] title, char[] input);*/

    int compareStrings(String input, String title);
    int getPoints(String input, String title);
    List<Items> orderItems(List<Items> previousList, String input);
    List<Shops> orderShopsByTitle(List<Shops> previousList, String input);
    List<Shops> orderShopsByDescription(List<Shops> previousList, String input);

    List<Items> findItems(String category, String subcategory, Double rating, String county);
    List<Items> convertItemsWithPointsDtoSortedSetToItemsList(SortedSet<ItemsWithPointsDto> itemsWithPointsDtos);
    List<Shops> convertShopsByTitleWithPointsDtoSortedSetToShopsList(SortedSet<ShopsByTitleWithPointsDto> shopsByTitleWithPointsDtoSortedSet);
    List<Shops> convertShopsByDescriptionWithPointsDtoSortedSetToShopsList(SortedSet<ShopsByDescriptionWithPointsDto> shopsByDescriptionWithPointsDtoSortedSet);

    List<SearchShopResponseDto> convertHashMapToList(HashMap<ShopDto, List<SearchItemsResponseDto>> shopsAndItems);
    List<SearchShopResponseDto> convertShopDtoListToSearchShopResponseDtoWithNoItems(List<ShopDto> shopDtoList);

    ShopDto convertShopToShopDto(Shops shops);
    List<Shops> findShops(String category, String subcategory, Double rating, String county);

    int getPoints(Object object, String input, String whatStringToCheck);
    int getPointsForShop(Shops shops, String input, String whatStringToCheck);
    int getPointsForItems(Items items, String input);

    HashMap<ShopDto, List<SearchItemsResponseDto>> convertShopIdToShopDto(HashMap<String, List<SearchItemsResponseDto>> shopsAndItems);
    HashMap<String, List<SearchItemsResponseDto>> findShopsByItems(List<Items> items);

    List<ShopDto> convertShopsToShopDtoList(List<Shops> shops);

    List<GetCategoriesResponseDto> convertCategoriesToGetCategoriesResponseDto(List<Categories> categories);
    List<GetSubcategoriesResponseDto> convertSubcategoriesToGetSubcategoriesResponseDto(List<Subcategories> subcategories);
    List<GetSubcategoriesByCategoryRequestDto> convertSubcategoriesToGetSubcategoriesByCategoryRequestDto(List<Subcategories> subcategories);

    List<Items> findItemsBySubcategoryInList(Subcategories subcategories, List<Items> foundItems);
    List<Items> findItemsByCategoryInList(Categories categories, List<Items> foundItems);

    List<Shops> findAllShopsBySubcategoryInList(Subcategories subcategories, List<Shops> shopsFound);
    List<Shops> findAllShopsByCategoryInList(Categories categories, List<Shops> shopsFound);

    //JPA METHODS
    List<Items> findAllItemsAndShopPublished();
    List<Items> findItemsByCategory(Categories categories);
    List<Items> findItemsBySubcategory(Subcategories subcategories);
    List<Items> findItemsByCountyAndRating(Double rating, String county);
    List<Items> findItemsByCounty(String county);
    List<Items> findItemsByRating(Double rating);

    Categories findCategoryByName(String category);
    Categories findCategoryById(String categoryId);
    Subcategories findSubcategoryByName(String subcategory);
    Subcategories findSubcategoryById(String subcategoryId);

    List<Categories> findAllCategories();
    List<Subcategories> findAllSubcategories();
    List<Subcategories> findAllSubcategoriesByCategory(Categories categories);

    Shops findShopById(String id);
    List<Shops> findAllShopsBySubcategory(Subcategories subcategories);
    List<Shops> findAllShopsByCategory(Categories categories);
    List<Shops> findAllPublishedShops();

    List<Shops> findShopsByCountyAndRating(String county, Double rating);
    List<Shops> findShopsByRating(Double rating);
    List<Shops> findShopsByCounty(String county);

}
