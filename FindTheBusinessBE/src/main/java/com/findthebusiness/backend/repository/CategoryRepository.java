package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Categories, String> {

    Optional<Categories> findByName(String name);

}
