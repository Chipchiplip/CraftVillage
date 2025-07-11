/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Product;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author ACER
 */
public class ProductImage {
    private int imageID;
    private int productID;
    private String imageUrl;
    private boolean isMain;
    private String altText;
    private int displayOrder;
    private int status; // 1: active, 0: inactive
    private Timestamp createdDate;
    private Timestamp updatedDate;
    
    // Constructors-------------------------------------------------------------
    public ProductImage() {
        this.status = 1; // Active by default
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public ProductImage(int imageID, int productID, String imageUrl, String altText, 
                       int displayOrder, int status, Timestamp createdDate, Timestamp updatedDate) {
        this.imageID = imageID;
        this.productID = productID;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.displayOrder = displayOrder;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    //--------------------------------------------------------------------------
    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isIsMain() {
        return isMain;
    }

    public void setIsMain(boolean isMain) {
        this.isMain = isMain;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "ProductImage{" + "imageID=" + imageID + ", productID=" + productID + ", imageUrl=" + imageUrl + ", isMain=" + isMain + ", createdDate=" + createdDate + '}';
    }
    
}
