package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.dto.items.AddItemRequestDto;
import com.findthebusiness.backend.dto.items.AddItemResponseDtoWithAccessToken;
import com.findthebusiness.backend.dto.items.DeleteItemResponseDtoWithAccessToken;
import com.findthebusiness.backend.dto.items.GetItemsRequestDto;
import com.findthebusiness.backend.exception.*;
import com.findthebusiness.backend.service.service_implementation.ItemsServiceImpl;
import com.findthebusiness.backend.service.service_implementation.SearchServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/items")
public class ItemsController {

    private final ItemsServiceImpl itemsService;


    public ItemsController(ItemsServiceImpl itemsService) {
        this.itemsService = itemsService;
    }

    @PostMapping("/getItems")
    private ResponseEntity<?> getItems(@RequestParam("store") String shopId, @RequestBody GetItemsRequestDto getItemsRequestDto) {
        return itemsService.getItems(shopId, getItemsRequestDto);
    }

    @DeleteMapping("/deleteItem")
    private ResponseEntity<?> deleteItem(@RequestParam("shopId") String shopId, @RequestParam("itemId") String itemId, HttpServletRequest request, HttpServletResponse response) {
        try {
            DeleteItemResponseDtoWithAccessToken deleteItemResponseDtoWithAccessToken = itemsService.deleteItem(shopId, itemId, request);

            response.addCookie(deleteItemResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(deleteItemResponseDtoWithAccessToken.getDeleteItemResponseDto());

        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/addItem")
    private ResponseEntity<?> postItem(@RequestParam("shopId") String shopId, @RequestParam("itemId") String itemId, @RequestBody AddItemRequestDto addItemRequestDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            AddItemResponseDtoWithAccessToken addItemResponseDtoWithAccessToken = itemsService.addItem(shopId, itemId, addItemRequestDto, request);

            response.addCookie(addItemResponseDtoWithAccessToken.getAccessToken());
            return ResponseEntity.ok(addItemResponseDtoWithAccessToken.getAddItemResponseDto());
        } catch (NameTooLongException e) {
            return ResponseEntity.status(490).build();
        } catch (DescriptionTooLongException e) {
            return ResponseEntity.status(491).build();
        } catch (PhotoRequiredException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (NotTheOwnerException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (CannotAddItemException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
