/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import entity.Orders.Payment;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface IPaymentService {
    Map<Date,BigDecimal> getRevenue();
    List<Payment> getAllPayment();
    Map<Integer,BigDecimal> getRevenueByYear(int year);
}
