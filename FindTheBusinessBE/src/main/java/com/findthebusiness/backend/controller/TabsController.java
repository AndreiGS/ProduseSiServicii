package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.dto.shops.ChangeContactDataResponseDtoWithAccessToken;
import com.findthebusiness.backend.dto.tabs.DeleteTabResponseDtoWithAccessToken;
import com.findthebusiness.backend.dto.tabs.PostTabRequestDto;
import com.findthebusiness.backend.dto.tabs.PostTabResponseDtoWithAccessToken;
import com.findthebusiness.backend.exception.CannotSaveTabException;
import com.findthebusiness.backend.exception.NameTooLongException;
import com.findthebusiness.backend.exception.NotTheOwnerException;
import com.findthebusiness.backend.service.service_implementation.TabServiceImpl;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/tabs")
public class TabsController {

    private final TabServiceImpl tabService;

    public TabsController(TabServiceImpl tabService) {
        this.tabService = tabService;
    }

    @PostMapping("/postTab")
    private ResponseEntity<?> postTab(@RequestParam("shopId") String shopId, @RequestParam("tabId") String tabId, @RequestBody PostTabRequestDto postTabRequestDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            PostTabResponseDtoWithAccessToken postTabResponseDtoWithAccessToken = tabService.postTab(shopId, tabId, postTabRequestDto, request);

            response.addCookie(postTabResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(postTabResponseDtoWithAccessToken.getPostTabResponseDto());
        } catch (CannotSaveTabException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NameTooLongException e) {
            return ResponseEntity.status(490).build();
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @DeleteMapping("/deleteTab")
    private ResponseEntity<?> deleteTab(@RequestParam("shopId") String shopId, @RequestParam("tabId") String tabId, HttpServletRequest request, HttpServletResponse response) {
        try {
            DeleteTabResponseDtoWithAccessToken deleteTabResponseDtoWithAccessToken = tabService.deleteTab(shopId, tabId, request);

            response.addCookie(deleteTabResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(deleteTabResponseDtoWithAccessToken.getDeleteTabResponseDto());
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

}
