/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Orders;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class Payment {
    private int paymentID;
    private Integer orderID;
    private Integer tourBookingID;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;
    private String transactionID;
    private Timestamp paymentDate;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    // Constructors-------------------------------------------------------------
    public Payment() {}

    public Payment(int paymentID, Integer orderID, Integer tourBookingID, BigDecimal amount, String paymentMethod, Timestamp paymentDate) {
        this.paymentID = paymentID;
        this.orderID = orderID;
        this.tourBookingID = tourBookingID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }
    
    //--------------------------------------------------------------------------
    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getTourBookingID() {
        return tourBookingID;
    }

    public void getTourBookingID(Integer tourBookingID) {
        this.tourBookingID = tourBookingID;
    }

   

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
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
        return "Payment{" + "paymentID=" + paymentID + ", orderID=" + orderID + ", tourBookingID=" + tourBookingID + ", amount=" + amount + ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus + ", transactionID=" + transactionID + ", paymentDate=" + paymentDate + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }
    
}
