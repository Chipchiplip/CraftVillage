/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.Product;

import java.sql.Timestamp;

/**
 *
 * @author trinh
 */
public class Category {
    private int cid;
    private String name;
    private String description;
    private String imageUrl;
    private int status;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public Category() {
        this.status = 1; // Active by default
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public Category(int cid, String name, String description, String imageUrl, int status, 
                   Timestamp createdDate, Timestamp updatedDate) {
        this.cid = cid;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Category(int cid, String name) {
        this.cid = cid;
        this.name = name;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return "Category{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    public int getCateId() {
        return cid;
    }

    public String getCateName() {
        return name;
    }

    public String getCname() {
        return name;
    }

    // Chuẩn hóa cho generic DAO
    public int getId() {
        return cid;
    }
    public void setId(int id) {
        this.cid = id;
    }
} 