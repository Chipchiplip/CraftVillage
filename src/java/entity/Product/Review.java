package entity.Product;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int productId;
    private String productName;
    private int userId;
    private String username;
    private String userAvatar;
    private int rating; // 1-5 stars
    private String comment;
    private String imageUrl;
    private int status; // 1: Active, 0: Inactive
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public Review() {
        this.status = 1; // Active by default
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public Review(int reviewId, int productId, String productName, int userId, String username,
                 String userAvatar, int rating, String comment, String imageUrl, int status,
                 Timestamp createdDate, Timestamp updatedDate) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.productName = productName;
        this.userId = userId;
        this.username = username;
        this.userAvatar = userAvatar;
        this.rating = rating;
        this.comment = comment;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isActive() {
        return status == 1;
    }

    public void setActive(boolean active) {
        this.status = active ? 1 : 0;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    // Chuẩn hóa cho generic DAO
    public int getId() {
        return reviewId;
    }
    public void setId(int id) {
        this.reviewId = id;
    }
    public String getName() {
        return username;
    }
    public void setName(String name) {
        this.username = name;
    }
} 