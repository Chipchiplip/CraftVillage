/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import entity.CraftVillage.CraftType;
import entity.CraftVillage.CraftVillage;
import context.DBContext;
import entity.MessageNotification.Message;
import entity.MessageNotification.MessageThread;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

/**
 *
 * @author ACER
 */
public class MessageDAO {

    private static final Logger LOGGER = Logger.getLogger(MessageDAO.class.getName());

    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        return new Message(
                rs.getInt("messageID"),
                rs.getInt("threadID"),
                rs.getInt("senderID"),
                rs.getString("messageContent"),
                rs.getString("attachmentUrl"),
                rs.getTimestamp("sentDate")
        ) {
        };
    }

    private MessageThread mapResultSetToMessageThread(ResultSet rs) throws SQLException {
        return new MessageThread(
                rs.getInt("threadID"),
                rs.getInt("userID"),
                rs.getInt("sellerID"),
                rs.getString("messageName")
        ) {
        };
    }

    private void closeResources(java.sql.Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }

    public boolean checkMessageThreadExist(int userID, int sellerID) {
        String query = "SELECT COUNT(*) FROM MessageThread WHERE userID = ? and sellerID = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userID);
            ps.setInt(2, sellerID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if message thread exists for userID="
                    + userID + " and sellerID=" + sellerID, e);
        }
        return false;
    }

    public boolean addNewMessageThread(MessageThread messageThread) {
        String query = "{call addNewMessageThread(?, ?, ?, ?)}";

        try (Connection conn = new DBContext().getConnection(); CallableStatement cs = conn.prepareCall(query)) {

            cs.setInt(1, messageThread.getUserID());
            cs.setInt(2, messageThread.getSellerID());
            cs.setString(3, messageThread.getMessageName());
            cs.registerOutParameter(4, java.sql.Types.INTEGER);

            cs.execute();

            int result = cs.getInt(4);
            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding new message thread for userID="
                    + messageThread.getUserID() + " and sellerID=" + messageThread.getSellerID(), e);
        }
        return false;
    }

    public List<Message> getMessageByThreadID(int threadID) {
        String query = "SELECT * FROM Message WHERE threadID = ?";
        List<Message> list = new ArrayList<>();

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, threadID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToMessage(rs));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting messages for threadID=" + threadID, e);
        }
        return list;
    }

    public boolean sendMessage(Message message) {
        String sql = "{call sendMessage(?, ?, ?, ?, ?)}";

        try (Connection conn = new DBContext().getConnection(); CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, message.getThreadID());
            cs.setInt(2, message.getSenderID());
            cs.setString(3, message.getMessageContent());

            if (message.getAttachmentUrl() != null) {
                cs.setString(4, message.getAttachmentUrl());
            } else {
                cs.setNull(4, java.sql.Types.VARCHAR);
            }

            cs.registerOutParameter(5, java.sql.Types.INTEGER);

            cs.execute();

            int result = cs.getInt(5);
            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error sending message for threadID=" + message.getThreadID()
                    + ", senderID=" + message.getSenderID(), e);
        }
        return false;
    }

    public List<MessageThread> getMessageThreadByUserID(int userID) {
        String query = "SELECT * FROM MessageThread WHERE userID = ?";
        List<MessageThread> list = new ArrayList<>();

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToMessageThread(rs));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting message threads for userID=" + userID, e);
        }
        return list;
    }

    public MessageThread getMessageThread(int userID, int sellerID) {
        String query = "SELECT * FROM MessageThread WHERE userID = ? AND sellerID = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userID);
            ps.setInt(2, sellerID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMessageThread(rs);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error getting message thread for userID=" + userID
                    + " and sellerID=" + sellerID, e);
        }
        return null;
    }

    public int getThreadID(int userID, int sellerID) {
        String query = "SELECT threadID FROM MessageThread WHERE userID = ? AND sellerID = ?";

        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userID);
            ps.setInt(2, sellerID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (rs.getInt("threadID"));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error getting message thread for userID=" + userID
                    + " and sellerID=" + sellerID, e);
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(new MessageDAO().getThreadID(6, 2));
    }

}
