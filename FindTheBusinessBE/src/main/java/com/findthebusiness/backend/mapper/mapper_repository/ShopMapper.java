package com.findthebusiness.backend.mapper.mapper_repository;

import com.findthebusiness.backend.dto.search.ShopDto;
import com.findthebusiness.backend.dto.shops.SaveShopRequestDto;
import com.findthebusiness.backend.dto.shops.ShopCardDto;
import com.findthebusiness.backend.dto.shops.StoreFrontRequestDto;
import com.findthebusiness.backend.dto.subcategories.SubcategoriesDto;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Subcategories;
import com.findthebusiness.backend.entity.Users;

import java.util.List;

public interface ShopMapper {
    List<ShopCardDto> convertShopsToShopCardDto(List<Shops> shops);
    StoreFrontRequestDto convertShopsToStoreFrontRequestDto(Shops shop);
    List<SubcategoriesDto> convertSubcategoriesToSubcategoriesDtoList(List<Subcategories> subcategories);
    Shops convertSaveShopRequestDtoToShop(SaveShopRequestDto saveShopRequestDto, Users user);
    ShopDto convertShopsToShopDto(Shops shops);

    Subcategories findSubcategoryByName(String subcategory);
}
