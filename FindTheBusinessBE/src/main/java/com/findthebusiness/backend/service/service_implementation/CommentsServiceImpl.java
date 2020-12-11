package com.findthebusiness.backend.service.service_implementation;

import com.findthebusiness.backend.dto.comments.CommentsResponseDto;
import com.findthebusiness.backend.dto.comments.GetCommentsResponseDto;
import com.findthebusiness.backend.dto.comments.PostCommentRequestDto;
import com.findthebusiness.backend.entity.Comments;
import com.findthebusiness.backend.entity.Shops;
import com.findthebusiness.backend.exception.CannotFindShopException;
import com.findthebusiness.backend.mapper.mapper_implementation.CommentsMapperImpl;
import com.findthebusiness.backend.mapper.mapper_repository.CommentsMapper;
import com.findthebusiness.backend.repository.CommentsRepository;
import com.findthebusiness.backend.repository.ShopRepository;
import com.findthebusiness.backend.service.service_repository.CommentsService;
import com.findthebusiness.backend.utils.BasicEncrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final ShopRepository shopRepository;

    private final CommentsMapperImpl commentsMapper;

    public CommentsServiceImpl(CommentsRepository commentsRepository, ShopRepository shopRepository, CommentsMapperImpl commentsMapper) {
        this.commentsRepository = commentsRepository;
        this.shopRepository = shopRepository;
        this.commentsMapper = commentsMapper;
    }

    @Override
    public ResponseEntity<?> postComment(PostCommentRequestDto postCommentRequestDto, String shopId) {
        if(postCommentRequestDto.getComment().equals("") || postCommentRequestDto.getComment() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if(postCommentRequestDto.getPrice() <= 0 || postCommentRequestDto.getPrice() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if(postCommentRequestDto.getRating() <= 0 || postCommentRequestDto.getRating() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        if(postCommentRequestDto.getUsername().equals("") || postCommentRequestDto.getUsername() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        try {
            Shops shop = findShopById(shopId);
            Comments comment = new Comments();

            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

            comment.setEmail(BasicEncrypt.encrypt(postCommentRequestDto.getUsername()))
                    .setPostDate(DATE_FORMAT.format(new Date()))
                    .setRating(postCommentRequestDto.getRating())
                    .setText(postCommentRequestDto.getComment())
                    .setShop(shop);

            saveCommentWithoutReturning(comment);

            try {
                if(shop.getRatingNumber() == null)
                    shop.setRatingNumber(0);

                if(shop.getRating() == null)
                    shop.setRating(0.0);

                if(shop.getPriceNumber() == null)
                    shop.setPriceNumber(0);
                if(shop.getPrice() == null)
                    shop.setPrice(0);

                shop.setRatingNumber(shop.getRatingNumber()+1);
                shop.setRating(shop.getRating()+postCommentRequestDto.getRating());

                shop.setPriceNumber(shop.getPriceNumber()+1);
                shop.setPrice(shop.getPrice() + postCommentRequestDto.getPrice());

                saveShopWithoutReturning(shop);

                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<?> getComments(String shopId) {
        try {
            List<CommentsResponseDto> comments = convertCommentsToCommentsResponseDto(findCommentsByShop(shopId));

            return ResponseEntity.ok(new GetCommentsResponseDto(comments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public List<CommentsResponseDto> convertCommentsToCommentsResponseDto(List<Comments> comments) {
        return commentsMapper.convertCommentsToCommentsResponseDto(comments);
    }

    @Override
    public void saveCommentWithoutReturning(Comments comments) {
        commentsRepository.save(comments);
    }

    @Override
    public Shops findShopById(String id) {
        return shopRepository.findById(id).orElseThrow(CannotFindShopException::new);
    }

    @Override
    public List<Comments> findCommentsByShop(String id) {
        Shops shop = findShopById(id);

        return commentsRepository.findCommentsByShop(shop).orElseGet(ArrayList::new);
    }

    @Override
    public void saveShopWithoutReturning(Shops shop) {
        shopRepository.save(shop);
    }
}
