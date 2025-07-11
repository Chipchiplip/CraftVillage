/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Orders;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author ACER
 */
public class TicketCode {
    private int codeID;
    private int orderDetailID;
    private String ticketCode;
    private String qrCode;
    private Timestamp issueDate;
    private Timestamp expiryDate;
    private Timestamp usageDate;
    private int status;
    private String usedBy;
    private String notes;

    // Constructors-------------------------------------------------------------
    public TicketCode() {}

  
    //--------------------------------------------------------------------------
    public int getCodeID() {
        return codeID;
    }

    public void setCodeID(int codeID) {
        this.codeID = codeID;
    }

    public int getOrderDetailID() {
        return orderDetailID;
    }

    public void setOrderDetailID(int orderDetailID) {
        this.orderDetailID = orderDetailID;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Timestamp getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Timestamp getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(Timestamp usageDate) {
        this.usageDate = usageDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Alias methods for compatibility
    public int getTicketCodeID() {
        return codeID;
    }

    public void setTicketCodeID(int ticketCodeID) {
        this.codeID = ticketCodeID;
    }

    public Timestamp getUsedDate() {
        return usageDate;
    }

    public void setUsedDate(Timestamp usedDate) {
        this.usageDate = usedDate;
    }

    @Override
    public String toString() {
        return "TicketCode{" + "codeID=" + codeID + ", orderDetailID=" + orderDetailID + ", ticketCode=" + ticketCode + ", qrCode=" + qrCode + ", issueDate=" + issueDate + ", expiryDate=" + expiryDate + ", usageDate=" + usageDate + ", status=" + status + ", usedBy=" + usedBy + ", notes=" + notes + '}';
    }
    
}
