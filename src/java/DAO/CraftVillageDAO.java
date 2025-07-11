/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;



import entity.CraftVillage.CraftType;
import entity.CraftVillage.CraftVillage;
import context.DBContext;
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
public class CraftVillageDAO {

    private static final Logger LOGGER = Logger.getLogger(CraftVillageDAO.class.getName());

    private CraftVillage mapResultSetToCraftVillage(ResultSet rs) throws SQLException {
        return new CraftVillage(
                rs.getInt("villageID"),
                rs.getInt("typeID"),
                rs.getString("villageName"),
                rs.getString("description"),
                rs.getString("address"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getString("contactPhone"),
                rs.getString("contactEmail"),
                rs.getInt("status"),
                rs.getInt("clickCount"),
                rs.getTimestamp("lastClicked"),
                rs.getString("mainImageUrl"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate"),
                rs.getInt("sellerId"),
                rs.getString("openingHours"),
                rs.getString("closingDays"),
                rs.getBigDecimal("averageRating"),
                rs.getInt("totalReviews"),
                rs.getString("mapEmbedUrl"),
                rs.getString("virtualTourUrl"),
                rs.getString("history"),
                rs.getString("specialFeatures"),
                rs.getString("famousProducts"),
                rs.getString("culturalEvents"),
                rs.getString("craftProcess"),
                rs.getString("videoDescriptionUrl"),
                rs.getString("travelTips")
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

    public CraftVillage getVillageById(int villageId) {
        CraftVillage village = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM CraftVillage WHERE villageID = ? AND status = 1";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, villageId);
            rs = ps.executeQuery();
            if (rs.next()) {
                village = new CraftVillage();
                village.setVillageID(rs.getInt("villageID"));
                village.setTypeID(rs.getObject("typeID") != null ? rs.getInt("typeID") : null);
                village.setVillageName(rs.getString("villageName"));
                village.setDescription(rs.getString("description"));
                village.setAddress(rs.getString("address"));
                village.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                village.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                village.setContactPhone(rs.getString("contactPhone"));
                village.setContactEmail(rs.getString("contactEmail"));
                village.setStatus(rs.getInt("status"));
                village.setClickCount(rs.getInt("clickCount"));
                village.setLastClicked(rs.getTimestamp("lastClicked"));
                village.setMainImageUrl(rs.getString("mainImageUrl"));
                village.setCreatedDate(rs.getTimestamp("createdDate"));
                village.setUpdatedDate(rs.getTimestamp("updatedDate"));
                village.setSellerId(rs.getObject("sellerId") != null ? rs.getInt("sellerId") : null);
                village.setOpeningHours(rs.getString("openingHours"));
                village.setClosingDays(rs.getString("closingDays"));
                village.setAverageRating(rs.getBigDecimal("averageRating"));
                village.setTotalReviews(rs.getInt("totalReviews"));
                village.setMapEmbedUrl(rs.getString("mapEmbedUrl"));
                village.setVirtualTourUrl(rs.getString("virtualTourUrl"));
                village.setHistory(rs.getString("history"));
                village.setSpecialFeatures(rs.getString("specialFeatures"));
                village.setFamousProducts(rs.getString("famousProducts"));
                village.setCulturalEvents(rs.getString("culturalEvents"));
                village.setCraftProcess(rs.getString("craftProcess"));
                village.setVideoDescriptionUrl(rs.getString("videoDescriptionUrl"));
                village.setTravelTips(rs.getString("travelTips"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return village;
    }

    public List<CraftType> getAllCraftType() {
        List<CraftType> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            // Query cho user - chỉ lấy sản phẩm active của seller
            String sql = "SELECT * FROM CraftType WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new CraftType(rs.getInt("typeID"), rs.getString("typeName"), rs.getString("description")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<CraftVillage> getAllCraftVillageActive() {
        List<CraftVillage> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            // Query cho user - chỉ lấy sản phẩm active của seller
            String sql = "SELECT * FROM CraftVillage WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCraftVillage(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public String getVillageNameByID(int villageID) {

        String query = "SELECT villageName FROM CraftVillage WHERE villageID = ? AND status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, villageID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("villageName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên dùng logging thay vì printStackTrace trong production
        }
        return "";
    }

    public String getCraftTypeNameByID(int typeID) {
        String query = "SELECT typeName FROM CraftType WHERE typeID = ? AND status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, typeID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("typeName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên dùng logging thay vì printStackTrace trong production
        }
        return "";
    }

    public boolean updateCraftVillageByAdmin(CraftVillage village) {
        String sql = "{CALL UpdateVillageFullByAdmin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, village.getVillageID());
            cs.setString(2, village.getVillageName());
            cs.setInt(3, village.getTypeID());
            cs.setString(4, village.getDescription());
            cs.setString(5, village.getAddress());
            cs.setDouble(6, village.getLatitude());
            cs.setDouble(7, village.getLongitude());
            cs.setString(8, village.getContactPhone());
            cs.setString(9, village.getContactEmail());
            cs.setInt(10, village.getStatus());
            cs.setInt(11, village.getSellerId());
            cs.setString(12, village.getOpeningHours());
            cs.setString(13, village.getClosingDays());
            cs.setString(14, village.getMapEmbedUrl());
            cs.setString(15, village.getVirtualTourUrl());
            cs.setString(16, village.getHistory());
            cs.setString(17, village.getSpecialFeatures());
            cs.setString(18, village.getFamousProducts());
            cs.setString(19, village.getCulturalEvents());
            cs.setString(20, village.getCraftProcess());
            cs.setString(21, village.getVideoDescriptionUrl());
            cs.setString(22, village.getTravelTips());
            cs.setString(23, village.getMainImageUrl());

            // Output parameter
            cs.registerOutParameter(24, Types.INTEGER);

            cs.execute();

            int result = cs.getInt(24);
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    
    // Additional methods for compatibility
    public List<CraftVillage> getAllCraftVillages(int offset, int limit) {
        // For now, ignore paging and return all active villages
        return getAllCraftVillageActive();
    }

    public int getTotalCraftVillages() {
        List<CraftVillage> villages = getAllCraftVillageActive();
        return villages.size();
    }

    public boolean addVillage(CraftVillage village) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "INSERT INTO CraftVillage (typeID, villageName, description, address, contactPhone, contactEmail, status) VALUES (?, ?, ?, ?, ?, ?, 1)";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, village.getTypeID());
            ps.setString(2, village.getVillageName());
            ps.setString(3, village.getDescription());
            ps.setString(4, village.getAddress());
            ps.setString(5, village.getContactPhone());
            ps.setString(6, village.getContactEmail());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean updateVillage(CraftVillage village) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "UPDATE CraftVillage SET typeID = ?, villageName = ?, description = ?, address = ?, contactPhone = ?, contactEmail = ? WHERE villageID = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, village.getTypeID());
            ps.setString(2, village.getVillageName());
            ps.setString(3, village.getDescription());
            ps.setString(4, village.getAddress());
            ps.setString(5, village.getContactPhone());
            ps.setString(6, village.getContactEmail());
            ps.setInt(7, village.getVillageID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean updateVillageStatus(int villageId, int status) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "UPDATE CraftVillage SET status = ? WHERE villageID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, villageId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public List<CraftVillage> getAllVillages() {
        List<CraftVillage> villages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM CraftVillage WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                CraftVillage v = new CraftVillage();
                v.setVillageID(rs.getInt("villageID"));
                v.setTypeID(rs.getObject("typeID") != null ? rs.getInt("typeID") : null);
                v.setVillageName(rs.getString("villageName"));
                v.setDescription(rs.getString("description"));
                v.setAddress(rs.getString("address"));
                v.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                v.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                v.setContactPhone(rs.getString("contactPhone"));
                v.setContactEmail(rs.getString("contactEmail"));
                v.setStatus(rs.getInt("status"));
                v.setClickCount(rs.getInt("clickCount"));
                v.setLastClicked(rs.getTimestamp("lastClicked"));
                v.setMainImageUrl(rs.getString("mainImageUrl"));
                v.setCreatedDate(rs.getTimestamp("createdDate"));
                v.setUpdatedDate(rs.getTimestamp("updatedDate"));
                v.setSellerId(rs.getObject("sellerId") != null ? rs.getInt("sellerId") : null);
                v.setOpeningHours(rs.getString("openingHours"));
                v.setClosingDays(rs.getString("closingDays"));
                v.setAverageRating(rs.getBigDecimal("averageRating"));
                v.setTotalReviews(rs.getInt("totalReviews"));
                v.setMapEmbedUrl(rs.getString("mapEmbedUrl"));
                v.setVirtualTourUrl(rs.getString("virtualTourUrl"));
                v.setHistory(rs.getString("history"));
                v.setSpecialFeatures(rs.getString("specialFeatures"));
                v.setFamousProducts(rs.getString("famousProducts"));
                v.setCulturalEvents(rs.getString("culturalEvents"));
                v.setCraftProcess(rs.getString("craftProcess"));
                v.setVideoDescriptionUrl(rs.getString("videoDescriptionUrl"));
                v.setTravelTips(rs.getString("travelTips"));
                villages.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return villages;
    }

    public boolean deleteVillageByAdmin(int villageID) {
        String sql = "{CALL DeleteVillageByAdmin(?, ?)}";

        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, villageID);
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();

            int result = cs.getInt(2);

            if (result == 1) {
                System.out.println("Village deleted permanently.");
                return true;
            } else if (result == 2) {
                System.out.println("Village references found → related records set inactive.");
                return true;
            } else {
                System.out.println("Delete village failed or unknown result.");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewVillageByAdmin(CraftVillage village) {
        String query = "{CALL AddVillageFullByAdmin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = new DBContext().getConnection();
            cs = conn.prepareCall(query);

            // Set all input parameters
            cs.setString(1, village.getVillageName());
            cs.setInt(2, village.getTypeID());
            cs.setString(3, village.getDescription());
            cs.setString(4, village.getAddress());
            cs.setDouble(5, village.getLatitude());
            cs.setDouble(6, village.getLongitude());
            cs.setString(7, village.getContactPhone());
            cs.setString(8, village.getContactEmail());
            cs.setInt(9, village.getStatus());
            cs.setInt(10, village.getSellerId());
            cs.setString(11, village.getOpeningHours());
            cs.setString(12, village.getClosingDays());
            cs.setString(13, village.getMapEmbedUrl());
            cs.setString(14, village.getVirtualTourUrl());
            cs.setString(15, village.getHistory());
            cs.setString(16, village.getSpecialFeatures());
            cs.setString(17, village.getFamousProducts());
            cs.setString(18, village.getCulturalEvents());
            cs.setString(19, village.getCraftProcess());
            cs.setString(20, village.getVideoDescriptionUrl());
            cs.setString(21, village.getTravelTips());
            cs.setString(22, village.getMainImageUrl());

            // Output parameter
            cs.registerOutParameter(23, Types.INTEGER);

            cs.execute();

            int result = cs.getInt(23);

            LOGGER.log(Level.INFO, "AddVillageFullByAdmin result code: {0}", result);

            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding new village: " + village.getVillageName(), e);
        } finally {
            closeResources(conn, cs, null);
        }
        return false;
    }

    public List<CraftVillage> getSearchVillageByAdmin(int status, int searchID, String contentSearch) {
        if (contentSearch == null) {
            contentSearch = "";
        } else {
            contentSearch = contentSearch.trim();
        }

        String query;
        switch (searchID) {
            case 1:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND CAST(typeID AS NVARCHAR) LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 2:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND villageName COLLATE Latin1_General_CI_AI LIKE ? ORDER BY villageName ASC";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 3:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND villageName COLLATE Latin1_General_CI_AI LIKE ? ORDER BY villageName DESC";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 4:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND villageName COLLATE Latin1_General_CI_AI LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 5:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND CAST(villageID AS NVARCHAR) LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 6:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND address COLLATE Latin1_General_CI_AI LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 7:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND CONVERT(date, createdDate) = CONVERT(date, GETDATE())";
                break;
            default:
                query = "SELECT * FROM CraftVillage WHERE status = ? AND villageName COLLATE Latin1_General_CI_AI LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
        }

        List<CraftVillage> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, status);

            if (query.contains("? AND") || query.contains("LIKE")) {
                ps.setString(2, contentSearch);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCraftVillage(rs));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public List<CraftVillage> getTopRatedByAdmin() {
        List<CraftVillage> list = new ArrayList<>();
        String query = "SELECT * FROM CraftVillage WHERE averageRating >= 4.5 and averageRating <= 5 AND status = 1 ORDER BY averageRating DESC";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCraftVillage(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên dùng logging thay vì printStackTrace trong production
        }
        return list;
    }

    public List<CraftVillage> getVillageByCategory(int typeID) {
        List<CraftVillage> list = new ArrayList<>();
        String sql = "SELECT * FROM CraftVillage WHERE typeID = ? AND status = 1";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, typeID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCraftVillage(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getVillageNameByTypeID(Integer typeID) {
        String query = "SELECT typeName FROM CraftType WHERE typeID = ? AND status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, typeID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("typeName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên dùng logging thay vì printStackTrace trong production
        }
        return "";
    }

    public static void main(String[] args) {
        //System.out.println(new CraftVillageDAO().updateCraftVillageByAdmin(new CraftVillage(1, "B", 1, "A", "A", 1, 1, "A", "A", 1, 1, "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A")));
        System.out.println(new CraftVillageDAO().getVillageById(1));
    }

}
