/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.ReportDAO;
import entity.Orders.Payment;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ACER
 */
public class PaymentService implements IPaymentService{
    ReportDAO rDAO = new ReportDAO();
    
    @Override
    public Map<Date, BigDecimal> getRevenue() {
        return rDAO.getRevenue();
    }
    
    @Override
    public Map<Integer, BigDecimal> getRevenueByYear(int year) {
        return rDAO.getRevenueByYear(year);
    }

    @Override
    public List<Payment> getAllPayment() {
        return rDAO.getAllPayments();
    }
}
