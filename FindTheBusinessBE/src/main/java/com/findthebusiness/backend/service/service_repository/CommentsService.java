package com.findthebusiness.backend.service.service_repository;

import com.findthebusiness.backend.dto.comments.CommentsResponseDto;
import com.findthebusiness.backend.dto.comments.PostCommentRequestDto;
import com.findthebusiness.backend.entity.Comments;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.repository.CommentsRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentsService {

    //CONTROLLER METHODS
    ResponseEntity<?> postComment(PostCommentRequestDto postCommentRequestDto, String shopId);
    ResponseEntity<?> getComments(String shopId);

    //CUSTOM METHODS
    List<CommentsResponseDto> convertCommentsToCommentsResponseDto(List<Comments> comments);

    //JPA METHODS
    void saveCommentWithoutReturning(Comments comments);
    void saveShopWithoutReturning(Shops shop);
    Shops findShopById(String id);
    List<Comments> findCommentsByShop(String id);

}
