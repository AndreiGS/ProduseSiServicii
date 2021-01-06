package com.findthebusiness.backend.mapper.mapper_implementation;

import com.findthebusiness.backend.dto.categories.CategoriesDto;
import com.findthebusiness.backend.dto.search.ShopDto;
import com.findthebusiness.backend.dto.shops.SaveShopRequestDto;
import com.findthebusiness.backend.dto.shops.ShopCardDto;
import com.findthebusiness.backend.dto.shops.StoreFrontRequestDto;
import com.findthebusiness.backend.dto.subcategories.SubcategoriesDto;
import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Subcategories;
import com.findthebusiness.backend.entity.Users;
import com.findthebusiness.backend.mapper.mapper_repository.ShopMapper;
import com.findthebusiness.backend.repository.SubcategoriesRepository;
import com.findthebusiness.backend.utils.enums.ShopSizesEnum;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShopMapperImpl implements ShopMapper {

    private final ModelMapper modelMapper;
    private final SubcategoriesRepository subcategoriesRepository;

    public ShopMapperImpl(ModelMapper modelMapper, SubcategoriesRepository subcategoriesRepository) {
        this.modelMapper = modelMapper;
        this.subcategoriesRepository = subcategoriesRepository;
    }

    @Override
    public List<ShopCardDto> convertShopsToShopCardDto(List<Shops> shops) {
        List<ShopCardDto> listOfCardsToReturn = new ArrayList<>();

        for (Shops shop : shops) {
            ShopCardDto shopCardDto = modelMapper.map(shop, ShopCardDto.class);
            shopCardDto.setSubcategories(convertSubcategoriesToSubcategoriesDtoList(shop.getSubcategories()));
            shopCardDto.setCategories(convertCategoriesToCategoriesDto(shop.getCategories()));
            shopCardDto.setMaximumSize(getSizeNameBySize(shop.getMaximumSize()));

            shopCardDto.setPromotedDaysInHomeRemaining(
                    getPromotedDaysRemaining(shop.getPromotedDateInHome(), shop.getPromotedDaysInHome()));
            shopCardDto.setPromotedDaysInSearchesRemaining(
                    getPromotedDaysRemaining(shop.getPromotedDateInSearches(), shop.getPromotedDaysInSearches()));

            listOfCardsToReturn.add(shopCardDto);
        }

        return listOfCardsToReturn;
    }

    private int getPromotedDaysRemaining(Date promotedDate, Integer promotedDays) {
        Calendar calendar = Calendar.getInstance();
        if (promotedDate != null && promotedDays != null) {
            calendar.setTime(promotedDate);
            calendar.add(Calendar.DAY_OF_MONTH, promotedDays);
            Date actualDate = new Date();
            Date expiryDate = calendar.getTime();

            if (actualDate.before(expiryDate)) {
                long diff = expiryDate.getTime() - actualDate.getTime();

                return Math.max(1, (int) (diff / (24 * 60 * 60 * 1000)));
            }
        }
        return 0;
    }

    private String getSizeNameBySize(Integer size) {

        if (size.equals(ShopSizesEnum.FREE.size))
            return ShopSizesEnum.FREE.name();

        if (size.equals(ShopSizesEnum.SMALL.size))
            return ShopSizesEnum.SMALL.name();

        if (size.equals(ShopSizesEnum.MEDIUM.size))
            return ShopSizesEnum.MEDIUM.name();

        if (size.equals(ShopSizesEnum.LARGE.size))
            return ShopSizesEnum.LARGE.name();

        if (size.equals(ShopSizesEnum.UNLIMITED.size))
            return ShopSizesEnum.UNLIMITED.name();

        return null;
    }

    @Override
    public List<SubcategoriesDto> convertSubcategoriesToSubcategoriesDtoList(List<Subcategories> subcategories) {
        List<SubcategoriesDto> subcategoriesList = new ArrayList<>();
        for (Subcategories subcategory : subcategories) {
            SubcategoriesDto newSubcategory = modelMapper.map(subcategory, SubcategoriesDto.class);
            newSubcategory.setCategoryId(subcategory.getCategory().getId());
            subcategoriesList.add(newSubcategory);
        }

        return subcategoriesList;
    }

    @Override
    public Shops convertSaveShopRequestDtoToShop(SaveShopRequestDto saveShopRequestDto, Users user) {
        Shops shop = modelMapper.map(saveShopRequestDto, Shops.class);

        List<Subcategories> subcategoriesList = new ArrayList<>();
        for (String subcategory : saveShopRequestDto.getSubcategories()) {
            if (!subcategory.equals("none")) {
                Subcategories subcategoryListItem = findSubcategoryByName(subcategory);
                if (subcategoryListItem == null)
                    continue;
                subcategoriesList.add(subcategoryListItem);
            }
        }
        shop.setCategories(convertCategoriesDtoToCategories(saveShopRequestDto.getCategory()));
        shop.setSubcategories(subcategoriesList);
        shop.setUser(user);
        return shop;
    }

    @Override
    public StoreFrontRequestDto convertShopsToStoreFrontRequestDto(Shops shop) {
        return modelMapper.map(shop, StoreFrontRequestDto.class);
    }

    @Override
    public Subcategories findSubcategoryByName(String subcategory) {
        Optional<Subcategories> subcategoryFound = subcategoriesRepository.findByName(subcategory);

        if (subcategoryFound.isPresent())
            return subcategoryFound.get();
        else
            return null;
    }

    @Override
    public ShopDto convertShopsToShopDto(Shops shops) {
        return modelMapper.map(shops, ShopDto.class);
    }

    @Override
    public CategoriesDto convertCategoriesToCategoriesDto(Categories categories) {
        if(categories == null) {
            return new CategoriesDto("none", "none");
        }
        return modelMapper.map(categories, CategoriesDto.class);
    }

    @Override
    public Categories convertCategoriesDtoToCategories(CategoriesDto categoriesDto) {
        if(categoriesDto.getId().equals("none")) return null;
        return modelMapper.map(categoriesDto, Categories.class);
    }

    @Override
    public List<CategoriesDto> convertCategoriesToCategoriesDtoList(List<Categories> categories) {
        List<CategoriesDto> categoriesList = new ArrayList<>();
        for (Categories category : categories) {
            categoriesList.add(modelMapper.map(category, CategoriesDto.class));
        }

        return categoriesList;
    }
}
