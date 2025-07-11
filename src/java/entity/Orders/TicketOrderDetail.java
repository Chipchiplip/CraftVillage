/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Orders;

import java.math.BigDecimal;

/**
 *
 * @author ACER
 */
public class TicketOrderDetail {

    private int detailID;
    private int orderID;
    private int ticketID;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    
    // Constructors-------------------------------------------------------------
    public TicketOrderDetail() {}
    
    //--------------------------------------------------------------------------
    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "TicketOrderDetail{" + "detailID=" + detailID + ", orderID=" + orderID + ", ticketID=" + ticketID + ", quantity=" + quantity + ", price=" + price + ", subtotal=" + subtotal + '}';
    }
    
}
