package entity.Ticket;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity class for VillageTicket table
 * Represents ticket pricing for each village and ticket type combination
 */
public class VillageTicket {
    private int ticketID;
    private int villageID;
    private int typeID;
    private BigDecimal price;
    private int status;
    private Date createdDate;
    private Date updatedDate;
    
    // Additional properties for joined data
    private String villageName;
    private String typeName;
    private String typeDescription;
    private String ageRange;

    // Constructors
    public VillageTicket() {}

    public VillageTicket(int villageID, int typeID, BigDecimal price) {
        this.villageID = villageID;
        this.typeID = typeID;
        this.price = price;
        this.status = 1; // Active by default
        this.createdDate = new Date();
    }

    public VillageTicket(int ticketID, int villageID, int typeID, BigDecimal price, 
                        int status, Date createdDate, Date updatedDate) {
        this.ticketID = ticketID;
        this.villageID = villageID;
        this.typeID = typeID;
        this.price = price;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Getters and Setters
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    // Additional properties getters and setters
    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    // Utility methods
    public boolean isActive() {
        return this.status == 1;
    }

    public void activate() {
        this.status = 1;
        this.updatedDate = new Date();
    }

    public void deactivate() {
        this.status = 0;
        this.updatedDate = new Date();
    }

    public String getFormattedPrice() {
        if (price != null) {
            return String.format("%,.0f VNĐ", price.doubleValue());
        }
        return "0 VNĐ";
    }

    public double getPriceAsDouble() {
        return price != null ? price.doubleValue() : 0.0;
    }

    @Override
    public String toString() {
        return "VillageTicket{" +
                "ticketID=" + ticketID +
                ", villageID=" + villageID +
                ", typeID=" + typeID +
                ", price=" + price +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                ", villageName='" + villageName + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VillageTicket that = (VillageTicket) obj;
        return ticketID == that.ticketID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ticketID);
    }
} 