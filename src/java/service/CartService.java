/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.CartWishList.Cart;
import entity.Product.Product;
import DAO.CartDAO;
import entity.CartWishList.CartItem;
import entity.CartWishList.CartTicket;
import DAO.CartTicketDAO;
import DAO.SimpleTicketOrderDAO;
import service.TicketAvailabilityService;
import entity.CartWishList.CartTicket;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author ACER
 */
public class CartService implements ICartService{
    private CartDAO cartDAO = new CartDAO();
    private CartTicketDAO cartTicketDAO = new CartTicketDAO();
    private ProductService productService = new ProductService();

    // ===== NEW METHODS FOR CONTROLLER LAYER =====
    
    @Override
    public Cart getCartByUser(int userId) {
        try {
            return cartDAO.getCartByUser(userId);
        } catch (Exception e) {
            System.err.println("Error getting cart for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Cart getOrCreateCart(int userId) {
        try {
            Cart cart = cartDAO.getCartByUser(userId);
            if (cart == null) {
                cart = new Cart();
                cart.setUserID(userId);
                int cartId = cartDAO.createCart(cart);
                cart.setCartID(cartId);
                System.out.println("[DEBUG] Created new cart with ID: " + cartId + " for user: " + userId);
            }
            return cart;
        } catch (Exception e) {
            System.err.println("Error getting or creating cart for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean addProductToCart(int userId, int productId, int quantity) {
        try {
            // Validate input
            if (userId <= 0 || productId <= 0 || quantity <= 0) {
                System.err.println("Invalid input: userId=" + userId + ", productId=" + productId + ", quantity=" + quantity);
                return false;
            }
            
            // Get product information
            Product product = productService.getProductByID(String.valueOf(productId));
            if (product == null) {
                System.err.println("Product not found with ID: " + productId);
                return false;
            }
            
            // Check stock availability
            if (product.getStock() < quantity) {
                System.err.println("Insufficient stock. Available: " + product.getStock() + ", Requested: " + quantity);
                return false;
            }
            
            // Check if product is active
            if (product.getStatus() != 1) {
                System.err.println("Product is not available. Status: " + product.getStatus());
                return false;
            }
            
            // Get or create cart
            Cart cart = getOrCreateCart(userId);
            if (cart == null) {
                System.err.println("Failed to get or create cart for user: " + userId);
                return false;
            }
            
            // Create cart item
            CartItem item = new CartItem();
            item.setCartID(cart.getCartID());
            item.setProductID(productId);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice().doubleValue());
            item.setProductName(product.getName());
            item.setImageUrl(product.getMainImageUrl());
            
            // Add item to database
            boolean success = cartDAO.addCartItem(item);
            System.out.println("[DEBUG] Add item to cart result: " + success);
            
            return success;
        } catch (Exception e) {
            System.err.println("Error adding product to cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateCartItemQuantity(int userId, int productId, int quantity) {
        try {
            if (userId <= 0 || productId <= 0 || quantity < 0) {
                return false;
            }
            
            Cart cart = getCartByUser(userId);
            if (cart == null) {
                System.err.println("Cart not found for user: " + userId);
                return false;
            }
            
            // Check stock if quantity > 0
            if (quantity > 0) {
                Product product = productService.getProductByID(String.valueOf(productId));
                if (product != null && product.getStock() < quantity) {
                    System.err.println("Insufficient stock. Available: " + product.getStock() + ", Requested: " + quantity);
                    return false;
                }
            }
            
            if (quantity <= 0) {
                // Remove item from cart
                removeCartItem(cart, productId);
            } else {
                // Update item quantity
                updateCartItem(cart, productId, quantity);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error updating cart item quantity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeCartItemByUser(int userId, int productId) {
        try {
            if (userId <= 0 || productId <= 0) {
                return false;
            }
            
            Cart cart = getCartByUser(userId);
            if (cart != null) {
                removeCartItem(cart, productId);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error removing cart item: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean addTicketToCartByUser(int userId, int ticketId, Date ticketDate, int quantity) {
        try {
            if (userId <= 0 || ticketId <= 0 || quantity <= 0 || ticketDate == null) {
                return false;
            }
            
            Cart cart = getOrCreateCart(userId);
            if (cart == null) {
                return false;
            }
            
            addTicketToCart(cart, ticketId, ticketDate, quantity);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding ticket to cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateTicketQuantityByUser(int userId, int cartTicketId, int quantity) {
        try {
            if (userId <= 0 || cartTicketId <= 0 || quantity < 0) {
                return false;
            }
            
            Cart cart = getCartByUser(userId);
            if (cart == null) {
                return false;
            }
            
            if (quantity <= 0) {
                removeTicketFromCart(cart, cartTicketId);
            } else {
                updateTicketInCart(cart, cartTicketId, quantity);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error updating ticket quantity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean removeTicketByUser(int userId, int cartTicketId) {
        try {
            if (userId <= 0 || cartTicketId <= 0) {
                return false;
            }
            
            Cart cart = getCartByUser(userId);
            if (cart != null) {
                removeTicketFromCart(cart, cartTicketId);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error removing ticket from cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===== EXISTING METHODS =====

    @Override
    public void addToCart(Cart cart, Product product, int quantity) {
        if (cart == null || product == null || quantity <= 0) return;
        CartItem item = new CartItem();
        item.setCartID(cart.getCartID());
        item.setProductID(product.getPid());
        item.setProductName(product.getName());
        item.setPrice(product.getPrice().doubleValue());
        item.setQuantity(quantity);
        item.setImageUrl(product.getMainImageUrl());
        cartDAO.addCartItem(item);
    }

    @Override
    public void updateCartItem(Cart cart, int productId, int quantity) {
        if (cart == null || cart.getItems() == null) return;
        for (CartItem item : cart.getItems()) {
            if (item.getProductID() == productId) {
                item.setQuantity(quantity);
                cartDAO.updateCartItem(item);
                break;
            }
        }
    }

    @Override
    public void removeCartItem(Cart cart, int productId) {
        if (cart == null || cart.getItems() == null) return;
        for (int i = 0; i < cart.getItems().size(); i++) {
            CartItem item = cart.getItems().get(i);
            if (item.getProductID() == productId) {
                cartDAO.deleteCartItem(item.getCartItemID());
                cart.getItems().remove(i);
                break;
            }
        }
    }

    @Override
    public double getTotalPrice(Cart cart) {
        if (cart == null) return 0.0;
        double total = 0.0;
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        if (cart.getTickets() != null) {
            for (CartTicket ticket : cart.getTickets()) {
                total += ticket.getPrice() * ticket.getQuantity();
            }
        }
        return total;
    }

    @Override
    public double calculateTotalWithDiscount(Cart cart) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double calculateTotalDiscount(Cart cart) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void applyDiscount(Cart cart, String discountCode) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void clearCart(int userId) {
        cartDAO.clearCart(userId);
    }

    // Ticket methods - smart add to cart with overselling prevention
    public void addTicketToCart(Cart cart, int ticketId, Date ticketDate, int quantity) {
        if (cart == null || ticketId <= 0 || quantity <= 0 || ticketDate == null) return;
        
        TicketAvailabilityService availabilityService = new TicketAvailabilityService();
        
        // Step 1: Check if the requested quantity is available in general
        if (!availabilityService.checkAvailability(ticketId, ticketDate, quantity)) {
            throw new RuntimeException("Không đủ vé cho ngày đã chọn! Vui lòng chọn ngày khác hoặc giảm số lượng.");
        }
        
        // Step 2: Check current cart quantity for this specific ticket and date
        int currentCartQuantity = getCurrentCartQuantity(cart.getCartID(), ticketId, ticketDate);
        int totalRequestedQuantity = currentCartQuantity + quantity;
        
        // Step 3: Check if total quantity (cart + new) exceeds available slots
        if (!availabilityService.checkAvailability(ticketId, ticketDate, totalRequestedQuantity)) {
            throw new RuntimeException(String.format(
                "Bạn đã có %d vé loại này trong giỏ hàng. Thêm %d vé nữa sẽ vượt quá số lượng có sẵn! " +
                "Vui lòng giảm số lượng hoặc kiểm tra giỏ hàng.",
                currentCartQuantity, quantity
            ));
        }
        
        // Step 4: Add to cart database (no availability update, just validation)
        boolean added = cartTicketDAO.addTicketToCart(cart.getCartID(), ticketId, ticketDate, quantity);
        if (!added) {
            throw new RuntimeException(String.format(
                "Không thể thêm vé vào giỏ hàng! CartID: %d, TicketID: %d, Quantity: %d. " +
                "Vui lòng kiểm tra dữ liệu và thử lại.",
                cart.getCartID(), ticketId, quantity
            ));
        }
        
        // Refresh tickets in cart
        cart.setTickets(cartTicketDAO.getCartTicketsByCartId(cart.getCartID()));
    }
    
    /**
     * Get current quantity in cart for specific ticket and date
     */
    private int getCurrentCartQuantity(int cartId, int ticketId, Date ticketDate) {
        List<CartTicket> cartTickets = cartTicketDAO.getCartTicketsByCartId(cartId);
        return cartTickets.stream()
                .filter(ticket -> ticket.getTicketId() == ticketId && 
                                 isSameDate(ticket.getTicketDate(), ticketDate))
                .mapToInt(CartTicket::getQuantity)
                .sum();
    }
    
    /**
     * Helper method to compare dates (ignoring time)
     */
    private boolean isSameDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) return false;
        
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
               cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR);
    }

    public void updateTicketInCart(Cart cart, int cartTicketId, int quantity) {
        if (cart == null || cartTicketId <= 0 || quantity < 0) return;
        
        // Simple update without availability management
        cartTicketDAO.updateTicketQuantity(cartTicketId, quantity);
        cart.setTickets(cartTicketDAO.getCartTicketsByCartId(cart.getCartID()));
    }

    public void removeTicketFromCart(Cart cart, int cartTicketId) {
        if (cart == null || cartTicketId <= 0) return;
        
        // Simple remove without availability management
        cartTicketDAO.removeTicketFromCart(cartTicketId);
        cart.setTickets(cartTicketDAO.getCartTicketsByCartId(cart.getCartID()));
    }

    public List<CartTicket> getTickets(Cart cart) {
        if (cart == null) return null;
        return cartTicketDAO.getCartTicketsByCartId(cart.getCartID());
    }
    
    /**
     * Process ticket checkout with availability management
     * This is where we actually book the tickets and reduce availability
     */
    public int checkoutTickets(Cart cart, String customerName, String email, String phone, 
                              String paymentMethod, String visitDate, String notes) {
        if (cart == null || cart.getTickets() == null || cart.getTickets().isEmpty()) {
            throw new RuntimeException("Giỏ hàng vé trống!");
        }
        
        TicketAvailabilityService availabilityService = new TicketAvailabilityService();
        SimpleTicketOrderDAO ticketOrderDAO = new SimpleTicketOrderDAO();
        
        try {
            // Convert CartTickets to Map format
            Map<Integer, Integer> ticketQuantities = new HashMap<>();
            for (CartTicket cartTicket : cart.getTickets()) {
                ticketQuantities.put(cartTicket.getTicketId(), cartTicket.getQuantity());
            }
            
            // Create ticket order
            int orderId = ticketOrderDAO.createTicketOrder(
                cart.getUserID(), 
                getVillageIdFromTickets(cart.getTickets()), // Assume same village
                customerName, 
                phone, 
                email, 
                paymentMethod, 
                notes, 
                ticketQuantities
            );
            
            if (orderId > 0) {
                // Book all tickets (reduce availability)
                for (CartTicket cartTicket : cart.getTickets()) {
                    availabilityService.bookTickets(cartTicket.getTicketId(), 
                                                   cartTicket.getTicketDate(), 
                                                   cartTicket.getQuantity());
                }
                
                // Clear cart tickets after successful checkout
                clearCartTickets(cart.getCartID());
                
                return orderId;
            } else {
                throw new RuntimeException("Không thể tạo đơn hàng vé!");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xử lý checkout: " + e.getMessage());
        }
    }
    
    private int getVillageIdFromTickets(List<CartTicket> tickets) {
        // Simplified: assume all tickets are from same village
        // In real app, you might need to handle multiple villages
        return 1; // Default village ID
    }
    
    private void clearCartTickets(int cartId) {
        cartTicketDAO.clearCartTickets(cartId);
    }
}
