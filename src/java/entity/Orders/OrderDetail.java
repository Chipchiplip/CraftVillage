package entity.Orders;

import java.sql.Timestamp;

public class OrderDetail {
    private int orderDetailID;
    private int orderID;
    private int productID;
    private String productName;
    private double price;
    private int quantity;
    private double subtotal;
    private String imageUrl;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public OrderDetail() {
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public OrderDetail(int orderDetailID, int orderID, int productID, String productName,
                      double price, int quantity, String imageUrl, Timestamp createdDate, 
                      Timestamp updatedDate) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        calculateSubtotal();
    }

    public OrderDetail(int orderDetailID, int orderID, int productID, double price, int quantity) {
        this.orderDetailID = orderDetailID;
        this.orderID = orderID;
        this.productID = productID;
        this.price = price;
        this.quantity = quantity;
        calculateSubtotal();
        this.createdDate = new java.sql.Timestamp(System.currentTimeMillis());
    }

    public OrderDetail(int orderID, int productID, int quantity, java.math.BigDecimal price) {
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.price = price.doubleValue();
        calculateSubtotal();
        this.createdDate = new java.sql.Timestamp(System.currentTimeMillis());
    }

    public int getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(int orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
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
        calculateSubtotal();
    }

    // Overload for BigDecimal compatibility
    public void setPrice(java.math.BigDecimal price) {
        this.price = price.doubleValue();
        calculateSubtotal();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
            calculateSubtotal();
        }
    }

    public double getSubtotal() {
        return subtotal;
    }

    private void calculateSubtotal() {
        this.subtotal = this.price * this.quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailID=" + orderDetailID +
                ", orderID=" + orderID +
                ", productID=" + productID +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", subtotal=" + subtotal +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}