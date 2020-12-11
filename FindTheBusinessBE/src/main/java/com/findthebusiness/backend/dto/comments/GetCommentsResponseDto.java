package com.findthebusiness.backend.dto.comments;

import com.findthebusiness.backend.entity.Comments;

import java.util.List;

public class GetCommentsResponseDto {

    List<CommentsResponseDto> comments;

    public GetCommentsResponseDto() {
    }

    public GetCommentsResponseDto(List<CommentsResponseDto> comments) {
        this.comments = comments;
    }

    public List<CommentsResponseDto> getComments() {
        return comments;
    }

    public GetCommentsResponseDto setComments(List<CommentsResponseDto> comments) {
        this.comments = comments;
        return this;
    }
}
