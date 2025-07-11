/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.CraftVillage;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author ACER
 */
public class CraftReview {
    private int reviewID;
    private int villageID;
    private int userID;
    private int rating; // Giá trị từ 1 đến 5
    private String reviewText;
    private Timestamp reviewDate;
    private String response;
    private Timestamp responseDate;
    
    // Constructors-------------------------------------------------------------
    public CraftReview() {
    }

    public CraftReview(int reviewID, int villageID, int userID, int rating, String reviewText, Timestamp reviewDate, String response, Timestamp responseDate) {
        this.reviewID = reviewID;
        this.villageID = villageID;
        this.userID = userID;
        this.rating = rating;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.response = response;
        this.responseDate = responseDate;
    }

    public CraftReview(int villageID, int userID, int rating, String reviewText) {
        this.villageID = villageID;
        this.userID = userID;
        this.rating = rating;
        this.reviewText = reviewText;
    }
    
    
    
    //--------------------------------------------------------------------------
    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getVillageID() {
        return villageID;
    }

    public void setVillageID(int villageID) {
        this.villageID = villageID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Timestamp getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Timestamp responseDate) {
        this.responseDate = responseDate;
    }

    @Override
    public String toString() {
        return "CraftReview{" + "reviewID=" + reviewID + ", villageID=" + villageID + ", userID=" + userID + ", rating=" + rating + ", reviewText=" + reviewText + ", reviewDate=" + reviewDate + ", response=" + response + ", responseDate=" + responseDate + '}';
    }
}
