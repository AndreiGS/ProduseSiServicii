package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.entity.Tabs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface TabRepository extends JpaRepository<Tabs, String> {

    Optional<List<Tabs>> findTabsByShops(Shops shop);
    Optional<Tabs> findTabsByNameAndShops(String name, Shops shop);

}
