package DAO;

import context.DBContext;
import entity.CraftVillage.Village;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VillageDAO {
    private static final Logger LOGGER = Logger.getLogger(VillageDAO.class.getName());

    private static final String SELECT_ALL_VILLAGES = "SELECT v.*, d.name as districtName FROM Village v " +
            "JOIN District d ON v.districtId = d.districtId WHERE v.status = 1";
    private static final String SELECT_VILLAGE_BY_ID = "SELECT v.*, d.name as districtName FROM Village v " +
            "JOIN District d ON v.districtId = d.districtId WHERE v.villageId = ?";
    private static final String SELECT_VILLAGES_BY_DISTRICT = "SELECT v.*, d.name as districtName FROM Village v " +
            "JOIN District d ON v.districtId = d.districtId WHERE v.districtId = ? AND v.status = 1";
    private static final String INSERT_VILLAGE = "INSERT INTO Village (name, description, imageUrl, districtId, " +
            "status, createdDate) VALUES (?, ?, ?, ?, ?, GETDATE())";
    private static final String UPDATE_VILLAGE = "UPDATE Village SET name = ?, description = ?, imageUrl = ?, " +
            "districtId = ?, status = ?, updatedDate = GETDATE() WHERE villageId = ?";
    private static final String DELETE_VILLAGE = "UPDATE Village SET status = 0, updatedDate = GETDATE() WHERE villageId = ?";
    private static final String SEARCH_VILLAGES = "SELECT v.*, d.name as districtName FROM Village v " +
            "JOIN District d ON v.districtId = d.districtId WHERE v.status = 1 AND (v.name LIKE ? OR " +
            "v.description LIKE ? OR d.name LIKE ?)";

    // Helper method to close resources
    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error closing database resources", e);
        }
    }

    public List<Village> getAllVillages() {
        List<Village> villages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_ALL_VILLAGES);
            rs = ps.executeQuery();

            while (rs.next()) {
                villages.add(mapResultSetToVillage(rs));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all villages", e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return villages;
    }

    public Village getVillageById(int villageId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_VILLAGE_BY_ID);
            ps.setInt(1, villageId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToVillage(rs);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting village with ID: " + villageId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public List<Village> getVillagesByDistrict(int districtId) {
        List<Village> villages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_VILLAGES_BY_DISTRICT);
            ps.setInt(1, districtId);
            rs = ps.executeQuery();

            while (rs.next()) {
                villages.add(mapResultSetToVillage(rs));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting villages for district ID: " + districtId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return villages;
    }

    public int addVillage(Village village) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int villageId = -1;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(INSERT_VILLAGE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, village.getName());
            ps.setString(2, village.getDescription());
            ps.setString(3, village.getImageUrl());
            ps.setInt(4, village.getDistrictId());
            ps.setInt(5, village.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    villageId = rs.getInt(1);
                    LOGGER.log(Level.INFO, "Village added successfully with ID: {0}", villageId);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding village", e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return villageId;
    }

    public boolean updateVillage(Village village) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(UPDATE_VILLAGE);
            ps.setString(1, village.getName());
            ps.setString(2, village.getDescription());
            ps.setString(3, village.getImageUrl());
            ps.setInt(4, village.getDistrictId());
            ps.setInt(5, village.getStatus());
            ps.setInt(6, village.getVillageId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Village updated successfully for ID: {0}", village.getVillageId());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No village found to update with ID: {0}", village.getVillageId());
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating village with ID: " + village.getVillageId(), e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean deleteVillage(int villageId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(DELETE_VILLAGE);
            ps.setInt(1, villageId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Village deleted successfully for ID: {0}", villageId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No village found to delete with ID: {0}", villageId);
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting village with ID: " + villageId, e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public List<Village> searchVillages(String keyword) {
        List<Village> villages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SEARCH_VILLAGES);
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);
            rs = ps.executeQuery();

            while (rs.next()) {
                villages.add(mapResultSetToVillage(rs));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching villages with keyword: " + keyword, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return villages;
    }

    private Village mapResultSetToVillage(ResultSet rs) throws SQLException {
        return new Village(
            rs.getInt("villageId"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("imageUrl"),
            rs.getInt("districtId"),
            rs.getString("districtName"),
            rs.getInt("status"),
            rs.getTimestamp("createdDate"),
            rs.getTimestamp("updatedDate")
        );
    }
} 