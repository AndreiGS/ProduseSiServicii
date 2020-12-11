package com.findthebusiness.backend.mapper.mapper_implementation;

import com.findthebusiness.backend.dto.comments.CommentsResponseDto;
import com.findthebusiness.backend.entity.Comments;
import com.findthebusiness.backend.mapper.mapper_repository.CommentsMapper;
import com.findthebusiness.backend.utils.BasicEncrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentsMapperImpl implements CommentsMapper {

    private final ModelMapper modelMapper;

    public CommentsMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CommentsResponseDto> convertCommentsToCommentsResponseDto(List<Comments> comments) {
        List<CommentsResponseDto> commentsResponseDtos = new ArrayList<>();
        for(Comments commentToConvert : comments) {
            CommentsResponseDto comment = modelMapper.map(commentToConvert, CommentsResponseDto.class);
            comment.setEmail(BasicEncrypt.decrypt(commentToConvert.getEmail()));
            commentsResponseDtos.add(comment);
        }
        return commentsResponseDtos;
    }
}
