package com.findthebusiness.backend.dto.comments;

public class PostCommentRequestDto {
    private String username;
    private String comment;
    private Integer price;
    private Double rating;

    public PostCommentRequestDto() {
    }

    public PostCommentRequestDto(String username, String comment, Integer price, Double rating) {
        this.username = username;
        this.comment = comment;
        this.price = price;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public PostCommentRequestDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public PostCommentRequestDto setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Integer getPrice() {
        return price;
    }

    public PostCommentRequestDto setPrice(Integer price) {
        this.price = price;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public PostCommentRequestDto setRating(Double rating) {
        this.rating = rating;
        return this;
    }
}
