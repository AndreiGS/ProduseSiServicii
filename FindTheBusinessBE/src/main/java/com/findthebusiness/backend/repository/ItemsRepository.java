package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Items;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, String> {

    Optional<List<Items>> findItemsByShop(Shops shop);
    Optional<List<Items>> findItemsByShopAndTabs(Shops shop, Tabs tab);
    Optional<Items> findItemsById(String id);

    @Query("SELECT item FROM Shops shop JOIN shop.items item WHERE shop.isPublished = ?1")
    Optional<List<Items>> findAllItemsAndShopPublished(Boolean published);

    @Query("SELECT item FROM Shops shop JOIN shop.items item WHERE shop.isPublished = ?1 AND shop.county = ?2 AND shop.rating >= ?3")
    Optional<List<Items>> findItemsByShopCountyAndRatingAndPublished(Boolean published, String county, Double rating);

    @Query("SELECT item FROM Shops shop JOIN shop.items item WHERE shop.isPublished = ?1 AND shop.county = ?2")
    Optional<List<Items>> findItemsByShopCountyAndPublished(Boolean published, String county);

    @Query("SELECT item FROM Shops shop JOIN shop.items item WHERE shop.isPublished = ?1 AND shop.rating >= ?2")
    Optional<List<Items>> findItemsByShopRatingAndPublished(Boolean published, Double rating);

    /*@Query("SELECT item FROM Items item, Shops shop WHERE shop.county = ?1 AND shop.rating >= ?2")
    Optional<List<Items>> findItemsByShopCountyAndRating(String county, Double rating);

    @Query("SELECT item FROM Items item, Shops shop WHERE shop.county = ?1")
    Optional<List<Items>> findItemsByShopCounty(String county);

    @Query("SELECT item FROM Items item, Shops shop WHERE shop.rating >= ?1")
    Optional<List<Items>> findItemsByShopRating(Double rating);*/
}
