package DAO;

import context.DBContext;
import entity.CraftVillage.CraftType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CraftTypeDAO extends DBContext {
    
    private static final String SELECT_ALL_CRAFT_TYPES = "SELECT * FROM CraftType WHERE status = 1";
    private static final String SELECT_CRAFT_TYPE_BY_ID = "SELECT * FROM CraftType WHERE craftTypeID = ?";
    private static final String INSERT_CRAFT_TYPE = "INSERT INTO CraftType (craftTypeName, description, status, createdDate) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_CRAFT_TYPE = "UPDATE CraftType SET craftTypeName = ?, description = ?, status = ?, updatedDate = ? WHERE craftTypeID = ?";
    private static final String DELETE_CRAFT_TYPE = "UPDATE CraftType SET status = 0 WHERE craftTypeID = ?";
    private static final String COUNT_CRAFT_TYPES = "SELECT COUNT(*) FROM CraftType WHERE status = 1";
    private static final String SELECT_CRAFT_TYPES_PAGINATED = "SELECT * FROM CraftType WHERE status = 1 ORDER BY craftTypeID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    public List<CraftType> getAllCraftTypes() {
        List<CraftType> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_CRAFT_TYPES);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                CraftType craftType = new CraftType();
                craftType.setCraftTypeID(rs.getInt("craftTypeID"));
                craftType.setCraftTypeName(rs.getString("craftTypeName"));
                craftType.setDescription(rs.getString("description"));
                craftType.setStatus(rs.getInt("status"));
                craftType.setCreatedDate(rs.getTimestamp("createdDate"));
                craftType.setUpdatedDate(rs.getTimestamp("updatedDate"));
                list.add(craftType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<CraftType> getAllCraftTypes(int offset, int limit) {
        List<CraftType> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CRAFT_TYPES_PAGINATED)) {
            
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CraftType craftType = new CraftType();
                    craftType.setCraftTypeID(rs.getInt("craftTypeID"));
                    craftType.setCraftTypeName(rs.getString("craftTypeName"));
                    craftType.setDescription(rs.getString("description"));
                    craftType.setStatus(rs.getInt("status"));
                    craftType.setCreatedDate(rs.getTimestamp("createdDate"));
                    craftType.setUpdatedDate(rs.getTimestamp("updatedDate"));
                    list.add(craftType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public CraftType getCraftTypeByID(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CRAFT_TYPE_BY_ID)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CraftType craftType = new CraftType();
                    craftType.setCraftTypeID(rs.getInt("craftTypeID"));
                    craftType.setCraftTypeName(rs.getString("craftTypeName"));
                    craftType.setDescription(rs.getString("description"));
                    craftType.setStatus(rs.getInt("status"));
                    craftType.setCreatedDate(rs.getTimestamp("createdDate"));
                    craftType.setUpdatedDate(rs.getTimestamp("updatedDate"));
                    return craftType;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addCraftType(CraftType craftType) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_CRAFT_TYPE, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, craftType.getCraftTypeName());
            ps.setString(2, craftType.getDescription());
            ps.setInt(3, craftType.getStatus());
            ps.setTimestamp(4, craftType.getCreatedDate());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        craftType.setCraftTypeID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCraftType(CraftType craftType) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_CRAFT_TYPE)) {
            
            ps.setString(1, craftType.getCraftTypeName());
            ps.setString(2, craftType.getDescription());
            ps.setInt(3, craftType.getStatus());
            ps.setTimestamp(4, craftType.getUpdatedDate());
            ps.setInt(5, craftType.getCraftTypeID());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCraftType(int id) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CRAFT_TYPE)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalCraftTypes() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(COUNT_CRAFT_TYPES);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
} 