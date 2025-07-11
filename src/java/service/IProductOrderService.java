/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import entity.Orders.ProductOrder;
import entity.Orders.ProductOrderDetail;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface IProductOrderService {

    // Tạo đơn hàng
    int createOrder(ProductOrder order) throws Exception;
    // Thêm chi tiết đơn hàng
    boolean addOrderDetail(int orderId, int productId, int quantity, double price);
    // Truy vấn đơn hàng
    ProductOrder getOrderById(int orderId);
    List<ProductOrder> getOrdersByUserId(int userId);
    List<ProductOrderDetail> getOrderDetailsByOrderId(int orderId);
    // Cập nhật trạng thái đơn hàng
    boolean updateOrderStatus(int orderId, int status);
    boolean updatePaymentStatus(int orderId, int paymentStatus);
    // Hủy đơn và hoàn tiền
    boolean cancelOrder(int orderId, String reason);
    boolean processRefund(int orderId, double amount, String reason);
    // Thống kê
    Map<String, Double> getMonthlyRevenue(); // tổng theo tháng
    Map<String, Integer> getOrderCountByStatus(); // đếm số đơn theo trạng thái
    List<Map<String, Object>> getTopSellingProducts(int limit);
}
