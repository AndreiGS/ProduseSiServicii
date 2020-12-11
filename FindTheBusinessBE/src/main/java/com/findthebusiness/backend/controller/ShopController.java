package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.dto.shops.*;
import com.findthebusiness.backend.exception.*;
import com.findthebusiness.backend.service.service_implementation.ShopServiceImpl;
import com.findthebusiness.backend.utils.ScheduleDelayTimes;
import com.sun.mail.iap.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("api/shops")
public class ShopController {

    private final ShopServiceImpl shopService;
    private static final Logger log = LoggerFactory.getLogger(ShopController.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final Integer scheduleDelay=1000*10;

    public ShopController(ShopServiceImpl shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/getPromotedShops")
    private ResponseEntity<PromotedShopsPageDto> getListOfPromotedShops(@RequestParam Integer page) {
        return shopService.getShopsForPage(page);
    }

    @GetMapping("/getShopDetailsById")
    private ResponseEntity<StoreFrontRequestDto> getShopDetailsById(@RequestParam("id") String shopId) {
        return shopService.getShopDetailsById(shopId);
    }

    @GetMapping("/getRatingAndPrice")
    private ResponseEntity<ReloadRatingAndPriceResponseDto> getRatingAndPrice(@RequestParam("id") String shopId) {
        return shopService.getRatingAndPrice(shopId);
    }

    @PatchMapping("/refreshShop")
    private ResponseEntity<?> refreshShop(@RequestParam("id") String shopId, HttpServletResponse response, HttpServletRequest request) {
        try {
            RefreshShopResponseWithAccessToken refreshShopResponseWithAccessToken = shopService.refreshShop(shopId, request, response);

            response.addCookie(refreshShopResponseWithAccessToken.getAccessToken());
            return ResponseEntity.ok(refreshShopResponseWithAccessToken.getRefreshShopResponse());
        } catch (NotEnoughCreditException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PatchMapping("/promoteShop")
    private ResponseEntity<?> promoteShop(@RequestParam("id") String shopId, HttpServletResponse response, HttpServletRequest request) {
        try {
            PromoteShopResponseWithAccessToken promoteShopResponseWithAccessToken = shopService.promoteShop(shopId, request, response);

            response.addCookie(promoteShopResponseWithAccessToken.getAccessToken());
            return ResponseEntity.ok(promoteShopResponseWithAccessToken.getPromoteShopResponse());
        } catch (NotEnoughCreditException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/saveShop")
    private ResponseEntity<?> uploadShop(@RequestBody SaveShopRequestDto saveShopRequestDto, @RequestParam("id") String shopId, HttpServletRequest request, HttpServletResponse response) {
        try {
            SaveShopResponseDtoWithAccessToken saveShopResponseDtoWithAccessToken = shopService.uploadShop(shopId, saveShopRequestDto, request, response);

            response.addCookie(saveShopResponseDtoWithAccessToken.getAccessToken());

            return ResponseEntity.ok(saveShopResponseDtoWithAccessToken.getSaveShopResponseDto());
        } catch (NameTooLongException e) {
            return ResponseEntity.status(490).build();
        } catch (DescriptionTooLongException e) {
            return ResponseEntity.status(491).build();
        } catch (PhotoRequiredException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (NotEnoughTokensException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ShopsLimitExceeded e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CannotChangeSizeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteShop")
    private ResponseEntity<?> deleteShop(@RequestParam("id") String shopId, HttpServletRequest request, HttpServletResponse response) {
        try {
            DeleteShopResponseDtoWithAccessToken deleteShopResponseDtoWithAccessToken = shopService.deleteShop(shopId, request, response);

            response.addCookie(deleteShopResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(deleteShopResponseDtoWithAccessToken.getDeleteShopResponseDto());
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/checkIfOwner")
    private ResponseEntity<?> checkIfOwner(@RequestParam("id") String shopId, HttpServletRequest request, HttpServletResponse response) {
        try {
             CheckIfShopOwnerReponseDtoWithAccessToken checkIfShopOwnerReponseDtoWithAccessToken = shopService.checkIfShopOwner(shopId, request);

            response.addCookie(checkIfShopOwnerReponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(checkIfShopOwnerReponseDtoWithAccessToken.getCheckIfShopOwnerResponseDto());
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PatchMapping("/changeContactData")
    private ResponseEntity<?> changeContactData(@RequestParam("id") String shopId, @RequestBody ChangeContactDataRequestDto changeContactDataRequestDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            ChangeContactDataResponseDtoWithAccessToken changeContactDataResponseDtoWithAccessToken = shopService.changeContactData(shopId, changeContactDataRequestDto, request);

            response.addCookie(changeContactDataResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(changeContactDataResponseDtoWithAccessToken.getChangeContactDataResponseDto());
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/changePublished")
    private ResponseEntity<?> changePublished(@RequestParam(name = "sure", required = false) Boolean wantsToContinue, @RequestParam("id") String shopId, HttpServletRequest request, HttpServletResponse response) {
        try {
            ChangePublishedResponseDtoWithAccessToken changePublishedResponseDtoWithAccessToken = shopService.changePublished(shopId, wantsToContinue, request);

            response.addCookie(changePublishedResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(changePublishedResponseDtoWithAccessToken.getChangePublishedResponseDto());
        } catch (NotEnoughTokensException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (CannotChangePublishException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/changeHasAutomaticTokenRefresh")
    private ResponseEntity<?> changeHasAutomaticTokenRefresh(@RequestParam("id") String shopId, HttpServletRequest request, HttpServletResponse response) {
        try {
            ChangeHasAutomaticTokenRefreshResponseDtoWithAccessToken changeHasAutomaticTokenRefreshResponseDtoWithAccessToken = shopService.changeHasAutomaticTokenRefresh(shopId, request);

            response.addCookie(changeHasAutomaticTokenRefreshResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(changeHasAutomaticTokenRefreshResponseDtoWithAccessToken.getChangeHasAutomaticTokenRefreshResponseDto());
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/changeLargeImage")
    private ResponseEntity<?> changeLargeImage(@RequestParam("id") String shopId, @RequestBody ChangeLargeImageRequestDto changeLargeImageRequestDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            ChangeLargeImageResponseDtoWithAccessToken changeLargeImageResponseDtoWithAccessToken = shopService.changeLargeImage(shopId, changeLargeImageRequestDto, request);

            response.addCookie(changeLargeImageResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(changeLargeImageResponseDtoWithAccessToken.getChangeLargeImageResponseDto());
        } catch (PhotoRequiredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @Scheduled(fixedDelay = ScheduleDelayTimes.MAKE_SHOPS_NOT_PROMOTED)
    private void setShopsNotPromotedAfterExpiring() {
        log.info("Set shops not promoted after expiring. The time is now {}", dateFormat.format(new Date()));
        try {
            shopService.setShopsNotPromotedAfterExpiring();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(fixedDelay = ScheduleDelayTimes.UNPUBLISH_SHOPS)
    private void unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshEnabled() {
        log.info("Unpublish shops if user does not have another token and automatic token refresh enabled. The time is now {}", dateFormat.format(new Date()));
        try {
            shopService.unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshEnabled();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Scheduled(fixedDelay = ScheduleDelayTimes.UNPUBLISH_SHOPS)
    private void unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshDisabled() {
        log.info("Unpublish shops if user does not have another token and automatic token refresh disabled. The time is now {}", dateFormat.format(new Date()));
        try {
            shopService.unpublishShopsIfUserDoesNotHaveAnotherTokenAndAutomaticTokenRefreshDisabled();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
