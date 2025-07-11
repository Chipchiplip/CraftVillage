/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Ticket;

import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class TicketType {
    private int typeID;
    private String typeName;
    private String description;
    private String ageRange;
    private int status;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    // Constructors-------------------------------------------------------------
    public TicketType() {}

    public TicketType(int typeID, String typeName, String description, String ageRange, int status, Timestamp createdDate, Timestamp updatedDate) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.description = description;
        this.ageRange = ageRange;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    //--------------------------------------------------------------------------
    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
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

    @Override
    public String toString() {
        return "TicketType{" +
                "typeID=" + typeID +
                ", typeName='" + typeName + '\'' +
                ", description='" + description + '\'' +
                ", ageRange='" + ageRange + '\'' +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TicketType that = (TicketType) obj;
        return typeID == that.typeID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(typeID);
    }
}
