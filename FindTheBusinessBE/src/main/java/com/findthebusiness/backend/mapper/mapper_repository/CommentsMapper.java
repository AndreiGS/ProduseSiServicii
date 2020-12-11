package com.findthebusiness.backend.mapper.mapper_repository;

import com.findthebusiness.backend.dto.comments.CommentsResponseDto;
import com.findthebusiness.backend.entity.Comments;

import java.util.List;

public interface CommentsMapper {

    List<CommentsResponseDto> convertCommentsToCommentsResponseDto(List<Comments> comments);

}
