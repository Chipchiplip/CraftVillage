/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.CartWishList;

import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class CartItem {
    private int itemID;
    private int cartID;
    private int productID;
    private int quantity;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private String productName;
    private double price;
    private String imageUrl;

    // Constructors-------------------------------------------------------------
    public CartItem() {}
    
    public CartItem(int itemID, int cartID, int productID, String productName, 
                   double price, int quantity, String imageUrl) {
        this.itemID = itemID;
        this.cartID = cartID;
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }
    
    //--------------------------------------------------------------------------
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getSubtotal() {
        return price * quantity;
    }

    // Alias methods for compatibility
    public int getCartItemID() {
        return itemID;
    }

    public void setCartItemID(int cartItemID) {
        this.itemID = cartItemID;
    }

    @Override
    public String toString() {
        return "CartItem{" + "itemID=" + itemID + ", cartID=" + cartID + ", productID=" + productID + ", quantity=" + quantity + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }
}
