package entity.Orders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderID;
    private int userID;
    private Timestamp orderDate;
    private BigDecimal totalAmount;
    private int status; // Changed from String to int: 0=processing, 1=completed, 2=cancelled, 3=refunded
    private String shippingAddress;
    private String phoneNumber;
    private String email;
    private String paymentMethod;
    private int paymentStatus; // Changed from String to int: 0=unpaid, 1=paid
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private List<OrderDetail> orderDetails;

    public Order() {
        this.orderDetails = new ArrayList<>();
        this.status = 0; // Changed from "pending" to 0
        this.paymentStatus = 0; // Changed from "pending" to 0
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public Order(int orderID, int userID, Timestamp orderDate, BigDecimal totalAmount, 
                int status, String shippingAddress, String phoneNumber, String email,
                String paymentMethod, int paymentStatus, Timestamp createdDate, Timestamp updatedDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.orderDetails = new ArrayList<>();
    }

    // Constructor tương thích code cũ
    public Order(int orderID, int userID, String orderDate, double totalAmount, String status) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = null; // Chuyển đổi String sang Timestamp nếu cần
        this.totalAmount = java.math.BigDecimal.valueOf(totalAmount);
        // Convert String status to int
        this.status = convertStringStatusToInt(status);
        this.orderDetails = new java.util.ArrayList<>();
        this.createdDate = new java.sql.Timestamp(System.currentTimeMillis());
    }
    
    // Helper method to convert old String status to new int status
    private int convertStringStatusToInt(String statusString) {
        if (statusString == null) return 0;
        switch (statusString.toLowerCase()) {
            case "pending": case "processing": return 0;
            case "completed": case "delivered": return 1;
            case "cancelled": return 2;
            case "refunded": return 3;
            default: return 0;
        }
    }

    // Getters and Setters
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    // Helper method for backward compatibility
    public void setStatus(String statusString) {
        this.status = convertStringStatusToInt(statusString);
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    // Helper method for backward compatibility
    public void setPaymentStatus(String paymentStatusString) {
        if (paymentStatusString == null) {
            this.paymentStatus = 0;
            return;
        }
        switch (paymentStatusString.toLowerCase()) {
            case "paid": case "completed": 
                this.paymentStatus = 1; 
                break;
            case "unpaid": case "pending": default: 
                this.paymentStatus = 0; 
                break;
        }
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

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    // Chuẩn hóa cho generic DAO
    public int getId() {
        return orderID;
    }
    public void setId(int id) {
        this.orderID = id;
    }
    public String getName() {
        return shippingAddress; // hoặc trường phù hợp nhất, ở đây không có name nên dùng shippingAddress
    }
    public void setName(String name) {
        this.shippingAddress = name;
    }

    // Helper methods with new int status
    public boolean isPending() {
        return status == 0;
    }

    public boolean isProcessing() {
        return status == 0; // Same as pending
    }

    public boolean isCompleted() {
        return status == 1;
    }
    
    // Keep for backward compatibility
    public boolean isDelivered() {
        return status == 1; // Same as completed
    }

    public boolean isCancelled() {
        return status == 2;
    }
    
    public boolean isRefunded() {
        return status == 3;
    }

    public boolean isPaid() {
        return paymentStatus == 1;
    }

    public boolean isUnpaid() {
        return paymentStatus == 0;
    }
    
    // NEW: Check if order is eligible for review
    public boolean isEligibleForReview() {
        return status == 1 && paymentStatus == 1; // Completed AND Paid
    }
    
    // Helper methods to get status as String (for display purposes)
    public String getStatusString() {
        switch (status) {
            case 0: return "Processing";
            case 1: return "Completed";
            case 2: return "Cancelled";
            case 3: return "Refunded";
            default: return "Unknown";
        }
    }
    
    public String getPaymentStatusString() {
        switch (paymentStatus) {
            case 0: return "Unpaid";
            case 1: return "Paid";
            default: return "Unknown";
        }
    }

    public void addOrderDetail(OrderDetail detail) {
        if (detail != null) {
            this.orderDetails.add(detail);
            recalculateTotalAmount();
        }
    }

    public void removeOrderDetail(OrderDetail detail) {
        if (detail != null && this.orderDetails.contains(detail)) {
            this.orderDetails.remove(detail);
            recalculateTotalAmount();
        }
    }

    private void recalculateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDetail detail : orderDetails) {
            total = total.add(BigDecimal.valueOf(detail.getSubtotal()));
        }
        this.totalAmount = total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", userID=" + userID +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", status=" + status + " (" + getStatusString() + ")" +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus=" + paymentStatus + " (" + getPaymentStatusString() + ")" +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", orderDetailsCount=" + (orderDetails != null ? orderDetails.size() : 0) +
                '}';
    }

    public static Order fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        Order order = new Order();
        order.setOrderID(rs.getInt("id"));
        order.setUserID(rs.getInt("userID"));
        order.setTotalAmount(rs.getBigDecimal("total_price"));
        order.setStatus(rs.getInt("status")); // Now reading as int
        order.setPaymentStatus(rs.getInt("paymentStatus")); // Now reading as int
        order.setShippingAddress(rs.getString("shippingAddress"));
        order.setPhoneNumber(rs.getString("shippingPhone"));
        order.setEmail(rs.getString("email"));
        order.setPaymentMethod(rs.getString("paymentMethod"));
        order.setCreatedDate(rs.getTimestamp("createdDate"));
        order.setUpdatedDate(rs.getTimestamp("updatedDate"));
        return order;
    }
} 