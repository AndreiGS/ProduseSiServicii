package com.findthebusiness.backend.dto.comments;

public class CommentsResponseDto {

    private String id;
    private String email;
    private String text;
    private Double rating;
    private String postDate;

    public CommentsResponseDto() {
    }

    public CommentsResponseDto(String id, String email, String text, Double rating, String postDate) {
        this.id = id;
        this.email = email;
        this.text = text;
        this.rating = rating;
        this.postDate = postDate;
    }

    public String getId() {
        return id;
    }

    public CommentsResponseDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CommentsResponseDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getText() {
        return text;
    }

    public CommentsResponseDto setText(String text) {
        this.text = text;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public CommentsResponseDto setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getPostDate() {
        return postDate;
    }

    public CommentsResponseDto setPostDate(String postDate) {
        this.postDate = postDate;
        return this;
    }
}
