/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.MessageNotification;

import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class Message {
    private int messageID;
    private int threadID;
    private int senderID;
    private String messageContent;
    private String attachmentUrl;
    private Timestamp sentDate;

    // Constructors-------------------------------------------------------------
    public Message() {}

    public Message(int messageID, int threadID, int senderID, String messageContent, String attachmentUrl, Timestamp sentDate) {
        this.messageID = messageID;
        this.threadID = threadID;
        this.senderID = senderID;
        this.messageContent = messageContent;
        this.attachmentUrl = attachmentUrl;
        this.sentDate = sentDate;
    }

    public Message(int threadID, int senderID, String messageContent, String attachmentUrl) {
        this.threadID = threadID;
        this.senderID = senderID;
        this.messageContent = messageContent;
        this.attachmentUrl = attachmentUrl;
    }
  
    //--------------------------------------------------------------------------

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Timestamp getSentDate() {
        return sentDate;
    }

    public void setSentDate(Timestamp sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public String toString() {
        return "Message{" + "messageID=" + messageID + ", threadID=" + threadID + ", senderID=" + senderID + ", messageContent=" + messageContent + ", attachmentUrl=" + attachmentUrl + ", sentDate=" + sentDate + '}';
    }
    
}
