package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Categories;
import com.findthebusiness.backend.entity.Subcategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SubcategoriesRepository extends JpaRepository<Subcategories, String> {

    Optional<Subcategories> findByName(String name);
    Optional<List<Subcategories>> findAllByCategory(Categories category);

}
