/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import context.DBContext;
import entity.CraftVillage.CraftType;
import entity.Ticket.Ticket;
import entity.Ticket.TicketType;
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
public class TicketDAO {

    private static final Logger LOGGER = Logger.getLogger(TicketDAO.class.getName());

    private TicketType mapResultSetToTicketType(ResultSet rs) throws SQLException {
        return new TicketType(
                rs.getInt("typeID"),
                rs.getString("typeName"),
                rs.getString("description"),
                rs.getString("ageRange"),
                rs.getInt("status"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate")
        );
    }

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        return new Ticket(
                rs.getInt("ticketID"),
                rs.getInt("villageID"),
                rs.getInt("typeID"),
                rs.getBigDecimal("price"),
                rs.getInt("status"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate")
        );
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

    public List<TicketType> getAllTicketType() {
        List<TicketType> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            // Query cho user - chỉ lấy sản phẩm active của seller
            String sql = "SELECT * FROM TicketType WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToTicketType(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public boolean createTicketByAdmin(Ticket ticket) {
        String query = "{? = CALL AddTicketByAdmin(?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = new DBContext().getConnection();
            cs = conn.prepareCall(query);

            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setInt(2, ticket.getVillageID());
            cs.setInt(3, ticket.getTypeID());
            cs.setBigDecimal(4, ticket.getPrice());
            cs.setInt(5, ticket.getStatus());

            cs.execute();

            int result = cs.getInt(1);
            LOGGER.log(Level.INFO, "AddTicketByAdmin result code: {0}", result);

            return result == 1;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating ticket with villageID: "
                    + ticket.getVillageID() + ", typeID: " + ticket.getTypeID(), e);
        } finally {
            closeResources(conn, cs, null);
        }
        return false;
    }

    public boolean updateTicketByAdmin(Ticket ticket) {
        String sql = "{? = CALL UpdateTicketByAdmin(?, ?, ?)}";
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.registerOutParameter(1, Types.INTEGER);

            cs.setInt(2, ticket.getTicketID());
            cs.setBigDecimal(3, ticket.getPrice());
            cs.setInt(4, ticket.getStatus());

            cs.execute();

            int result = cs.getInt(1);
            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating ticket: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean deleteTicketByAdmin(int ticketID) {
        String sql = "{? = CALL DeleteTicketByAdmin(?)}";
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, ticketID);
            // Output parameter
            cs.execute();
            if (cs.getInt(1) == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Ticket> getTickeReportByAdmin(int status) {
        String query;
        List<Ticket> list = new ArrayList<>();
        if (status == 0) {
            query = "SELECT * FROM VillageTicket WHERE status = 0";
        } else if (status == 1) {
            query = "SELECT * FROM VillageTicket WHERE status = 1";
        } else {
            query = "SELECT * FROM VillageTicket WHERE status = 1 ORDER BY typeID ASC";
        }
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log tốt hơn
        }
        return list;
    }

    public String getTicketNameByID(int typeID) {
        String query = "SELECT typeName FROM TicketType WHERE status = 1 AND typeID = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, typeID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("typeName");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting ticket type name for typeID: " + typeID, e);
        }
        return null;
    }

       public List<Ticket> searchTicketByAdmin(int status, int villageID) {
        String query;
        List<Ticket> list = new ArrayList<>();

        if (status == 2) {
            if (villageID == 0) {
                query = "SELECT * FROM VillageTicket";
            } else {
                query = "SELECT * FROM VillageTicket WHERE villageID = ?";
            }
        } else {
            if (villageID == 0) {
                query = "SELECT * FROM VillageTicket WHERE status = ?";
            } else {
                query = "SELECT * FROM VillageTicket WHERE villageID = ? AND status = ?";
            }
        }

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            if (status == 2) {
                if (villageID != 0) {
                    ps.setInt(1, villageID);
                }
            } else {
                if (villageID == 0) {
                    ps.setInt(1, status);
                } else {
                    ps.setInt(1, villageID);
                    ps.setInt(2, status);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc log ra file log
        }
        return list;
    }

    public List<Ticket> getTicketsByVillage(int villageId) {
        List<Ticket> list = new ArrayList<>();
        String query = "SELECT * FROM VillageTicket WHERE  villageID = ? and status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, villageId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // hoặc log ra file log
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(new TicketDAO().searchTicketByAdmin(2, 1));
    }

}
