package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.service.service_implementation.SearchServiceImpl;
import com.sun.mail.iap.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final SearchServiceImpl searchService;

    public SearchController(SearchServiceImpl searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/searchForItems")
    private ResponseEntity<?> searchForItems(@RequestParam(value = "searchBarText", required = false) String searchBarText, @RequestParam(value = "category", required = false) String category, @RequestParam(value = "subcategory", required = false) String subcategory, @RequestParam(value = "rating", required = false) Double rating, @RequestParam(value = "county", required = false) String county) {
        try {
            return ResponseEntity.ok(searchService.search(searchBarText, category, subcategory, rating, county));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getCategoriesAndSubcategories")
    private ResponseEntity<?> getCategoriesAndSubcategories() {
        try {
            return ResponseEntity.ok(searchService.getCategoriesAndSubcategories());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getSubcategoriesByCategory")
    private ResponseEntity<?> getSubcategoriesByCategory(@RequestParam("categoryId") String categoryId) {
        try {
            return ResponseEntity.ok(searchService.getSubcategoriesByCategory(categoryId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
