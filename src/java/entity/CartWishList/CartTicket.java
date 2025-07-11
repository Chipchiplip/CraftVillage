package entity.CartWishList;

import java.util.Date;

/**
 * Entity class for CartTicket table
 * Represents tickets in the shopping cart with specific dates
 */
public class CartTicket {
    private int cartTicketId;
    private int cartId;
    private int ticketId;
    private Date ticketDate;
    private int quantity;
    private double price;
    private Date createdDate;
    private Date updatedDate;
    
    // Additional properties for joined data
    private String villageName;
    private String ticketTypeName;
    private String ageRange;

    // Constructors
    public CartTicket() {}

    public CartTicket(int cartId, int ticketId, Date ticketDate, int quantity, double price) {
        this.cartId = cartId;
        this.ticketId = ticketId;
        this.ticketDate = ticketDate;
        this.quantity = quantity;
        this.price = price;
        this.createdDate = new Date();
    }

    public CartTicket(int cartTicketId, int cartId, int ticketId, Date ticketDate, 
                     int quantity, double price, Date createdDate, Date updatedDate) {
        this.cartTicketId = cartTicketId;
        this.cartId = cartId;
        this.ticketId = ticketId;
        this.ticketDate = ticketDate;
        this.quantity = quantity;
        this.price = price;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Getters and Setters
    public int getCartTicketId() {
        return cartTicketId;
    }

    public void setCartTicketId(int cartTicketId) {
        this.cartTicketId = cartTicketId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    // Additional properties for joined data
    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    // Utility methods
    public double getTotalPrice() {
        return price * quantity;
    }

    public double getSubtotal() {
        return getTotalPrice();
    }

    public String getFormattedPrice() {
        return String.format("%,.0f VNĐ", price);
    }

    public String getFormattedTotalPrice() {
        return String.format("%,.0f VNĐ", getTotalPrice());
    }

    public String getFormattedTicketDate() {
        if (ticketDate != null) {
            return String.format("%1$td/%1$tm/%1$tY", ticketDate);
        }
        return "";
    }

    @Override
    public String toString() {
        return "CartTicket{" +
                "cartTicketId=" + cartTicketId +
                ", cartId=" + cartId +
                ", ticketId=" + ticketId +
                ", ticketDate=" + ticketDate +
                ", quantity=" + quantity +
                ", price=" + price +
                ", villageName='" + villageName + '\'' +
                ", ticketTypeName='" + ticketTypeName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartTicket that = (CartTicket) obj;
        return cartTicketId == that.cartTicketId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(cartTicketId);
    }
} 