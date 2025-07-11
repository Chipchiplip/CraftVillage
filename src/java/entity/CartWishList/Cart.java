/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.CartWishList;

/**
 *
 * @author Pc
 */
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import entity.CartWishList.CartTicket;

public class Cart {
    private int cartID;
    private int userID;
    private List<CartItem> items;
    private List<CartTicket> tickets;
    private double totalAmount;
    private String status;
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public Cart() {
        this.items = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.status = "active";
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.totalAmount = 0.0;
    }

    public Cart(int cartID, int userID, List<CartItem> items, List<CartTicket> tickets, double totalAmount, 
                String status, Timestamp createdDate, Timestamp updatedDate) {
        this.cartID = cartID;
        this.userID = userID;
        this.items = items != null ? items : new ArrayList<>();
        this.tickets = tickets != null ? tickets : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        calculateTotalAmount();
    }

    public List<CartTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<CartTicket> tickets) {
        this.tickets = tickets;
        calculateTotalAmount();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        return "active".equalsIgnoreCase(status);
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public void addItem(CartItem item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        
        // Check if item already exists
        for (CartItem existingItem : items) {
            if (existingItem.getProductID() == item.getProductID()) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                calculateTotalAmount();
                return;
            }
        }
        
        items.add(item);
        calculateTotalAmount();
    }

    public void removeItem(int productId) {
        if (items != null) {
            items.removeIf(item -> item.getProductID() == productId);
            calculateTotalAmount();
        }
    }

    public void updateItemQuantity(int productId, int quantity) {
        if (items != null) {
            for (CartItem item : items) {
                if (item.getProductID() == productId) {
                    item.setQuantity(quantity);
                    break;
                }
            }
            calculateTotalAmount();
        }
    }

    public void clear() {
        if (items != null) {
            items.clear();
            calculateTotalAmount();
        }
    }

    public void addTicket(CartTicket ticket) {
        if (tickets == null) tickets = new ArrayList<>();
        // Check if ticket with same id/date exists, then update quantity
        for (CartTicket t : tickets) {
            if (t.getTicketId() == ticket.getTicketId() && t.getTicketDate().equals(ticket.getTicketDate())) {
                t.setQuantity(t.getQuantity() + ticket.getQuantity());
                calculateTotalAmount();
                return;
            }
        }
        tickets.add(ticket);
        calculateTotalAmount();
    }

    public void removeTicket(int cartTicketId) {
        if (tickets != null) {
            tickets.removeIf(t -> t.getCartTicketId() == cartTicketId);
            calculateTotalAmount();
        }
    }

    public void updateTicketQuantity(int cartTicketId, int quantity) {
        if (tickets != null) {
            for (CartTicket t : tickets) {
                if (t.getCartTicketId() == cartTicketId) {
                    t.setQuantity(quantity);
                    break;
                }
            }
            calculateTotalAmount();
        }
    }

    private void calculateTotalAmount() {
        this.totalAmount = 0.0;
        if (items != null) {
            for (CartItem item : items) {
                this.totalAmount += item.getSubtotal();
            }
        }
        if (tickets != null) {
            for (CartTicket ticket : tickets) {
                this.totalAmount += ticket.getSubtotal();
            }
        }
    }

    public int getTotalItems() {
        if (items == null) return 0;
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void applyDiscount(String discountCode) {}
    public double getTotalAmountWithDiscount() { return getTotalAmount(); }
    public double getTotalDiscount() { return 0.0; }
    public void updateItem(int productId, int quantity) { updateItemQuantity(productId, quantity); }

    @Override
    public String toString() {
        return "Cart{" +
                "cartID=" + cartID +
                ", userID=" + userID +
                ", items=" + items +
                ", tickets=" + tickets +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
