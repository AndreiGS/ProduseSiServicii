package com.findthebusiness.backend.repository;

import com.findthebusiness.backend.entity.Pages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagesRepository extends JpaRepository<Pages, String> {
}
