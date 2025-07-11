/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Ticket;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class Ticket {
    private int ticketID;
    private int villageID;
    private int typeID;
    private BigDecimal price;
    private int status;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    
    //Constructor---------------------------------------------------------------
    public Ticket() {
    }

    public Ticket(int ticketID, int villageID, int typeID, BigDecimal price, int status, Timestamp createdDate) {
        this.ticketID = ticketID;
        this.villageID = villageID;
        this.typeID = typeID;
        this.price = price;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Ticket(int ticketID, int villageID, int typeID, BigDecimal price, int status, Timestamp createdDate, Timestamp updatedDate) {
        this.ticketID = ticketID;
        this.villageID = villageID;
        this.typeID = typeID;
        this.price = price;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    
    public Ticket(int villageID, int typeID, BigDecimal price, int status) {
        this.villageID = villageID;
        this.typeID = typeID;
        this.price = price;
        this.status = status;
    }

    public Ticket(int ticketID, int villageID, int typeID, BigDecimal price, int status) {
        this.ticketID = ticketID;
        this.villageID = villageID;
        this.typeID = typeID;
        this.price = price;
        this.status = status;
    }

    public Ticket(int ticketID, BigDecimal price, int status) {
        this.ticketID = ticketID;
        this.price = price;
        this.status = status;
    }
    
    //--------------------------------------------------------------------------
    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public int getVillageID() {
        return villageID;
    }

    public void setVillageID(int villageID) {
        this.villageID = villageID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    @Override
    public String toString() {
        return "Ticket{" + "ticketID=" + ticketID + ", villageID=" + villageID + ", typeID=" + typeID + ", price=" + price + ", status=" + status + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }
}
