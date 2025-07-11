/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import entity.MessageNotification.Message;
import entity.MessageNotification.MessageThread;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface IMessageService {
    List<MessageThread> getThreadsByUser(int userId);
    List<Message> getMessagesByThread(int threadId);
    boolean sendMessage(Message message);
    boolean markMessageAsRead(int messageId);
    boolean checkMessageThreadExist(int userID, int sellerID);
    boolean addNewMessageThread(MessageThread messageThread);
    List<Message> getMessageByThreadID(int threadID);
    MessageThread getMessageThread(int userID, int sellerID);
    List<MessageThread> getMessageThreadByUserID(int userId);
    int getThreadID(int userID, int sellerID);
}
