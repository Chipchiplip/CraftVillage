package service;

import DAO.OrderDAO;
import entity.Orders.Order;
import entity.Orders.OrderDetail;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService implements IOrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private final OrderDAO orderDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
    }

    @Override
    public int createOrder(Order order) throws Exception {
        try {
            return orderDAO.createOrder(order);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating order", e);
            throw new Exception("Failed to create order: " + e.getMessage());
        }
    }

    @Override
    public Order getOrderById(int orderId) {
        try {
            return orderDAO.getOrderById(orderId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting order by ID: " + orderId, e);
            return null;
        }
    }

    @Override
    public List<Order> getAllOrders() {
        try {
            return orderDAO.getAllOrders();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all orders", e);
            return List.of();
        }
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        try {
            return orderDAO.getOrdersByUserId(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting orders for user ID: " + userId, e);
            return List.of();
        }
    }

    @Override
    public List<Order> getOrdersBySellerId(int sellerId) {
        try {
            return orderDAO.getOrdersBySellerId(sellerId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting orders for seller ID: " + sellerId, e);
            return List.of();
        }
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        try {
            return orderDAO.updateOrderStatus(orderId, status);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order status for order ID: " + orderId, e);
            return false;
        }
    }

    @Override
    public boolean cancelOrder(int orderId) {
        try {
            return orderDAO.cancelOrder(orderId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cancelling order ID: " + orderId, e);
            return false;
        }
    }

    @Override
    public void addOrderDetail(int orderId, int productId, int quantity, double price) throws Exception {
        try {
            orderDAO.addOrderDetail(orderId, productId, quantity, price);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding order detail", e);
            throw new Exception("Failed to add order detail: " + e.getMessage());
        }
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        try {
            return orderDAO.getOrderDetails(orderId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting order details for order ID: " + orderId, e);
            return List.of();
        }
    }

    @Override
    public Map<String, Double> getRevenueByMonth() {
        try {
            return orderDAO.getRevenueByMonth();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue by month", e);
            return Map.of();
        }
    }

    @Override
    public Map<String, Double> getRevenueByYear() {
        try {
            return orderDAO.getRevenueByYear();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting revenue by year", e);
            return Map.of();
        }
    }

    @Override
    public List<Map<String, Object>> getTopSellingProducts() {
        try {
            return orderDAO.getTopSellingProducts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting top selling products", e);
            return List.of();
        }
    }

    @Override
    public boolean addOrder(Order order) {
        try {
            int orderId = orderDAO.createOrder(order);
            return orderId > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding order", e);
            return false;
        }
    }

    @Override
    public boolean updateOrder(Order order) {
        try {
            return orderDAO.updateOrder(order);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order: " + order.getOrderID(), e);
            return false;
        }
    }

    @Override
    public boolean deleteOrder(int id) {
        try {
            return orderDAO.deleteOrder(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting order ID: " + id, e);
            return false;
        }
    }

    @Override
    public boolean updateOrderDetail(int orderId, int productId, int quantity, double price) {
        try {
            return orderDAO.updateOrderDetail(orderId, productId, quantity, price);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order detail", e);
            return false;
        }
    }

    @Override
    public boolean deleteOrderDetail(int orderId, int productId) {
        try {
            return orderDAO.deleteOrderDetail(orderId, productId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting order detail", e);
            return false;
        }
    }
} 