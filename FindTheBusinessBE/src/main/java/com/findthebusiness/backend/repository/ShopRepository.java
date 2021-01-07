package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Subcategories;
import com.findthebusiness.backend.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface ShopRepository extends JpaRepository<Shops, String> {

    Optional<List<Shops>> findAllByIsPublishedAndActualSizeGreaterThan(Boolean isPublished, Integer minimumReq);
    Optional<List<Shops>> findAllByUserOrderByRefreshedAtDesc(Users user);
    Optional<List<Shops>> findAllByIsPromotedInHomeAndIsPublished(Boolean type, Boolean isPublished, Pageable page);
    Optional<List<Shops>> findAllByIsPromotedInHome(Boolean isPromoted);
    Optional<List<Shops>> findAllByIsPromotedInSearches(Boolean isPromoted);
    Optional<List<Shops>> findAllBySubcategoriesAndActualSizeGreaterThan(Subcategories subcategory, Integer minimumReq);
    Optional<List<Shops>> findAllByIsPublishedAndHasAutomaticTokenRefresh(Boolean isPublished, Boolean hasAutomaticTokenRefresh);
    Optional<List<Shops>> findAllByCountyEqualsAndRatingGreaterThanEqual(String county, Double rating);
    Optional<List<Shops>> findAllByRatingGreaterThanEqual(Double rating);
    Optional<List<Shops>> findAllByCountyEquals(String county);
    Optional<List<Shops>> findAllByCategories(Categories categories);


    @Modifying
    void deleteById(String shopId);

}
