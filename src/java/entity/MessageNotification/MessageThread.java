/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.MessageNotification;

/**
 *
 * @author ACER
 */
public class MessageThread {

    private int threadID;
    private int userID;
    private int sellerID;
    private String messageName;

    // Constructors-------------------------------------------------------------
    public MessageThread() {
    }

    public MessageThread(int threadID, int userID, int sellerID, String messageName) {
        this.threadID = threadID;
        this.userID = userID;
        this.sellerID= sellerID;
        this.messageName = messageName;
    }

    public MessageThread(int userID, int sellerID) {
        this.userID = userID;
        this.sellerID = sellerID;
    }

    public MessageThread(int userID, int sellerID, String messageName) {
        this.userID = userID;
        this.sellerID = sellerID;
        this.messageName = messageName;
    }
    
    //--------------------------------------------------------------------------

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    @Override
    public String toString() {
        return "MessageThread{" + "threadID=" + threadID + ", userID=" + userID + ", sellerID=" + sellerID + ", messageName=" + messageName + '}';
    }
}
