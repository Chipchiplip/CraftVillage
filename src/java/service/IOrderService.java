package service;

import entity.Orders.Order;
import entity.Orders.OrderDetail;
import java.util.List;
import java.util.Map;

public interface IOrderService {
    // Basic CRUD operations
    int createOrder(Order order) throws Exception;
    Order getOrderById(int orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(int userId);
    List<Order> getOrdersBySellerId(int sellerId);
    boolean updateOrderStatus(int orderId, String status);
    boolean cancelOrder(int orderId);
    
    // Business operations
    void addOrderDetail(int orderId, int productId, int quantity, double price) throws Exception;
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);
    
    // Analytics operations
    Map<String, Double> getRevenueByMonth();
    Map<String, Double> getRevenueByYear();
    List<Map<String, Object>> getTopSellingProducts();

    boolean addOrder(Order order);
    boolean updateOrder(Order order);
    boolean deleteOrder(int id);

    boolean updateOrderDetail(int orderId, int productId, int quantity, double price);
    boolean deleteOrderDetail(int orderId, int productId);
} 