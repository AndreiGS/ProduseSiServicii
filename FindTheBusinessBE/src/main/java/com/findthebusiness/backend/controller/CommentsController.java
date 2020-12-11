package com.findthebusiness.backend.controller;

import com.findthebusiness.backend.dto.comments.PostCommentRequestDto;
import com.findthebusiness.backend.service.service_implementation.CommentsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentsController {

    private final CommentsServiceImpl commentsService;

    public CommentsController(CommentsServiceImpl commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("/postComment")
    private ResponseEntity<?> postComment(@RequestBody PostCommentRequestDto postCommentRequestDto, @RequestParam("store") String shopId) {
        return commentsService.postComment(postCommentRequestDto, shopId);
    }

    @GetMapping("/getComments")
    private ResponseEntity<?> getComments(@RequestParam("store") String shopId) {
        return commentsService.getComments(shopId);
    }

}
