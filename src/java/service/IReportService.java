/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author ACER
 */
public interface IReportService {
    Map<Integer, Integer> getMonthlyAccountRegistrations(int month, int year);
    Map<Integer, Integer> getOrderStatusSummaryByMonthYear(int month, int year);
    BigDecimal getRevenueByDayMonthYear(int date, int month, int year);
    int getNumberCraftPostByDayMonthYear(int day, int month, int year);
    int getNumberProductPostByDayMonthYear(int day, int month, int year);
    int getNumberTicketPostByDayMonthYear(int day, int month, int year);
}
