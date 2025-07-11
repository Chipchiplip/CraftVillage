/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import entity.MessageNotification.Notification;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface INotificationService {
    List<Notification> getNotificationsByUser(int userId);
    boolean markAsRead(int notificationId);
    boolean sendNotification(Notification notification);
}
