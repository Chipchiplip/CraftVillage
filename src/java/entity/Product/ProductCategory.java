/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Product;

import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class ProductCategory {
    private int categoryID;
    private String categoryName;
    private String description;
    private int status; // 1: hoạt động, 0: ẩn hoặc không dùng
    private Timestamp createdDate;
    private Timestamp updatedDate;

    // Constructors-------------------------------------------------------------
    public ProductCategory() {}

    public ProductCategory(int categoryID, String categoryName, String description, int status, Timestamp createdDate, Timestamp updatedDate) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
    
    
    
    
    //--------------------------------------------------------------------------
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    // Compatibility methods for JSP that might expect 'cid' and 'cname' fields
    public int getCid() {
        return this.categoryID;
    }
    
    public void setCid(int cid) {
        this.categoryID = cid;
    }
    
    public String getCname() {
        return this.categoryName;
    }
    
    public void setCname(String cname) {
        this.categoryName = cname;
    }
    
    public String getName() {
        return this.categoryName;
    }
    
    public void setName(String name) {
        this.categoryName = name;
    }

    @Override
    public String toString() {
        return "ProductCategory{" + "categoryID=" + categoryID + ", categoryName=" + categoryName + ", description=" + description + ", status=" + status + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }
}
