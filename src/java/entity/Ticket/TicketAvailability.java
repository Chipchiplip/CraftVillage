package entity.Ticket;

import java.util.Date;

/**
 * Entity class for TicketAvailability table
 * Manages daily ticket availability with booking limits
 */
public class TicketAvailability {
    private int availabilityId;
    private int ticketId;
    private Date availableDate;
    private int totalSlots;
    private int bookedSlots;
    private int availableSlots;
    private int status;
    private Date createdDate;
    private Date updatedDate;

    // Constructors
    public TicketAvailability() {}

    public TicketAvailability(int ticketId, Date availableDate, int totalSlots) {
        this.ticketId = ticketId;
        this.availableDate = availableDate;
        this.totalSlots = totalSlots;
        this.bookedSlots = 0;
        this.availableSlots = totalSlots;
        this.status = 1; // Active
        this.createdDate = new Date();
    }

    public TicketAvailability(int availabilityId, int ticketId, Date availableDate, 
                             int totalSlots, int bookedSlots, int status, 
                             Date createdDate, Date updatedDate) {
        this.availabilityId = availabilityId;
        this.ticketId = ticketId;
        this.availableDate = availableDate;
        this.totalSlots = totalSlots;
        this.bookedSlots = bookedSlots;
        this.availableSlots = totalSlots - bookedSlots;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Getters and Setters
    public int getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(int availabilityId) {
        this.availabilityId = availabilityId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Date getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = availableDate;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
        updateAvailableSlots();
    }

    public int getBookedSlots() {
        return bookedSlots;
    }

    public void setBookedSlots(int bookedSlots) {
        this.bookedSlots = bookedSlots;
        updateAvailableSlots();
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
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

    // Utility methods
    private void updateAvailableSlots() {
        this.availableSlots = this.totalSlots - this.bookedSlots;
    }

    public boolean isAvailable() {
        return status == 1 && availableSlots > 0;
    }

    public boolean hasAvailableSlots(int requestedQuantity) {
        return isAvailable() && availableSlots >= requestedQuantity;
    }

    public boolean bookSlots(int quantity) {
        if (hasAvailableSlots(quantity)) {
            this.bookedSlots += quantity;
            updateAvailableSlots();
            this.updatedDate = new Date();
            return true;
        }
        return false;
    }

    public boolean releaseSlots(int quantity) {
        if (bookedSlots >= quantity) {
            this.bookedSlots -= quantity;
            updateAvailableSlots();
            this.updatedDate = new Date();
            return true;
        }
        return false;
    }

    public String getAvailabilityStatus() {
        if (status == 0) return "Closed";
        if (availableSlots == 0) return "Fully Booked";
        if (availableSlots <= 5) return "Limited";
        return "Available";
    }

    public double getOccupancyRate() {
        if (totalSlots == 0) return 0.0;
        return (double) bookedSlots / totalSlots * 100.0;
    }

    @Override
    public String toString() {
        return "TicketAvailability{" +
                "availabilityId=" + availabilityId +
                ", ticketId=" + ticketId +
                ", availableDate=" + availableDate +
                ", totalSlots=" + totalSlots +
                ", bookedSlots=" + bookedSlots +
                ", availableSlots=" + availableSlots +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TicketAvailability that = (TicketAvailability) obj;
        return availabilityId == that.availabilityId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(availabilityId);
    }
} 