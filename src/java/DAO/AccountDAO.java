/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import context.DBContext;
import entity.Account.Account;
import entity.Account.SellerVerification;
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
public class AccountDAO {

    private static final Logger LOGGER = Logger.getLogger(AccountDAO.class.getName());

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("userID"),
                rs.getString("userName"),
                "********",
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("phoneNumber"),
                rs.getInt("roleID"),
                rs.getInt("status"),
                rs.getString("createdDate"),
                rs.getString("updatedDate"),
                rs.getString("fullName")
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

    public boolean registerAccount(Account account) {
        String query = "{CALL RegisterAccount(?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = new DBContext().getConnection();
            cs = conn.prepareCall(query);

            cs.setString(1, account.getUserName());
            cs.setString(2, account.getPassword());
            cs.setString(3, account.getEmail());
            cs.setString(4, account.getFullName());
            cs.setString(5, account.getAddress());
            cs.setString(6, account.getPhoneNumber());
            cs.registerOutParameter(7, java.sql.Types.INTEGER); // Register OUTPUT parameter

            cs.execute(); // Use execute() instead of executeUpdate() for procedures with OUTPUT parameters

            // Get the new user ID from the output parameter
            int newUserID = cs.getInt(7);
            LOGGER.log(Level.INFO, "Successfully registered user: {0} with ID: {1}",
                    new Object[]{account.getUserName(), newUserID});

            return newUserID > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error registering account for user: " + account.getUserName(), e);
        } finally {
            closeResources(conn, cs, null);
        }
        return false;
    }

    public Account loginAccount(String user, String pass) {
        String query = "{CALL LoginAccount(?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            cs = conn.prepareCall(query);
            cs.setString(1, user);
            cs.setString(2, pass);

            rs = cs.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In rõ lỗi ra console
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // In rõ lỗi ra console
                }
            }
            closeResources(conn, null, rs);
        }
        return null;
    }

    public boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM Account WHERE userName = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if username exists: " + username, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM Account WHERE email = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if username exists: " + email, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return false;
    }

    public boolean checkPhoneNumberExists(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM Account WHERE phoneNumber = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if phone number exists: " + phoneNumber, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return false;
    }

    public Account loginByEmail(String email) {
        // Use direct SQL query with REPLACE to remove line feeds and carriage returns
        String query = "SELECT userID, userName, email, fullName, address, phoneNumber, roleID, status, createdDate, updatedDate FROM Account WHERE REPLACE(REPLACE(LTRIM(RTRIM(email)), CHAR(10), ''), CHAR(13), '') = ? AND status = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            LOGGER.log(Level.INFO, "LoginByEmail called with email: " + email);

            conn = new DBContext().getConnection();
            LOGGER.log(Level.INFO, "Database connection successful");

            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            LOGGER.log(Level.INFO, "Executing query: " + query + " with email: " + email);

            rs = ps.executeQuery();

            if (rs.next()) {
                LOGGER.log(Level.INFO, "Found account for email: " + email + ", userName: " + rs.getString("userName"));
                return new Account(
                        rs.getInt("userID"),
                        rs.getString("userName"),
                        "********",
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("phoneNumber"),
                        rs.getInt("roleID"),
                        rs.getInt("status"),
                        rs.getString("createdDate"),
                        rs.getString("updatedDate"),
                        rs.getString("fullName")
                );
            } else {
                LOGGER.log(Level.WARNING, "No account found for email: " + email);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during loginByEmail for email: " + email, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        String query = "SELECT * FROM Account WHERE status = 1";
        List<Account> list = new ArrayList<>();
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            cs = conn.prepareCall(query);

            rs = cs.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToAccount(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In rõ lỗi ra console
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeResources(conn, null, rs);
        }
        return list;
    }

    public boolean updateAccount(Account account) {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = new DBContext().getConnection();

            boolean updatePassword = account.getPassword() != null
                    && !account.getPassword().trim().isEmpty()
                    && !account.getPassword().equals("********");

            int result;

            if (updatePassword) {
                String query = "{? = CALL UpdateAccountFull(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                cs = conn.prepareCall(query);
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, account.getUserID());
                cs.setString(3, account.getUserName());
                cs.setString(4, account.getPassword());
                cs.setString(5, account.getEmail());
                cs.setString(6, account.getPhoneNumber());
                cs.setString(7, account.getAddress());
                cs.setInt(8, account.getRoleID());
                cs.setInt(9, account.getStatus());
                cs.setString(10, account.getFullName());
            } else {
                String query = "{? = CALL UpdateAccountWithoutPassword(?, ?, ?, ?, ?, ?, ?, ?)}";
                cs = conn.prepareCall(query);
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, account.getUserID());
                cs.setString(3, account.getUserName());
                cs.setString(4, account.getEmail());
                cs.setString(5, account.getPhoneNumber());
                cs.setString(6, account.getAddress());
                cs.setInt(7, account.getRoleID());
                cs.setInt(8, account.getStatus());
                cs.setString(9, account.getFullName());
            }

            cs.execute();
            result = cs.getInt(1); // <-- lấy RETURN từ procedure

            if (result == 1) {
                LOGGER.log(Level.INFO, "Successfully updated account with ID: {0}", account.getUserID());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "Email or phone already exists. Update failed for ID: {0}", account.getUserID());
                return false;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error updating account with ID: " + account.getUserID(), e);
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating account with ID: " + account.getUserID(), e);
            return false;
        } finally {
            try {
                if (cs != null) {
                    cs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing resources", e);
            }
        }
    }

    public Account getAccountById(int id) {
        String query = "SELECT * FROM Account WHERE userID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("userID"),
                        rs.getString("userName"),
                        "********",
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("phoneNumber"),
                        rs.getInt("roleID"),
                        rs.getInt("status"),
                        rs.getString("createdDate"),
                        rs.getString("updatedDate"),
                        rs.getString("fullName")
                );
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting account by ID: " + id, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public boolean requestUpgradeForIndividual(SellerVerification sellerForm) {
        String query = "{? = call sp_InsertSellerVerification_Individual(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = new DBContext().getConnection();
            cs = conn.prepareCall(query);
            // Register the RETURN parameter
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            // Set input parameters
            cs.setInt(2, sellerForm.getSellerID());
            cs.setString(3, sellerForm.getBusinessType());
            cs.setString(4, sellerForm.getBusinessVillageCategry());
            cs.setString(5, sellerForm.getBusinessVillageName());
            cs.setString(6, sellerForm.getBusinessVillageAddress());
            cs.setString(7, sellerForm.getProductProductCategory());
            cs.setString(8, sellerForm.getProfileVillagePictureUrl());
            cs.setString(9, sellerForm.getContactPerson());
            cs.setString(10, sellerForm.getContactPhone());
            cs.setString(11, sellerForm.getContactEmail());
            cs.setString(12, sellerForm.getIdCardNumber());
            cs.setString(13, sellerForm.getIdCardFrontUrl());
            cs.setString(14, sellerForm.getIdCardBackUrl());
            cs.setString(15, sellerForm.getNote());
            // Execute the procedure
            cs.execute();
            int result = cs.getInt(1);
            LOGGER.log(Level.INFO, "sp_InsertSellerVerification_Individual result code: {0}", result);
            return result == 1;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error requesting seller upgrade for sellerID: " + sellerForm.getSellerID(), e);
        } finally {
            closeResources(conn, cs, null);
        }
        return false;
    }

    public boolean requestUpgradeForCraftVillage(SellerVerification sellerForm) {
        String query = "{? = call sp_InsertSellerVerification_CraftVillage(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = new DBContext().getConnection();
            cs = conn.prepareCall(query);

            // Register the RETURN parameter
            cs.registerOutParameter(1, java.sql.Types.INTEGER);

            // Set input parameters
            cs.setInt(2, sellerForm.getSellerID());
            cs.setString(3, sellerForm.getBusinessType());
            cs.setString(4, sellerForm.getBusinessVillageCategry());
            cs.setString(5, sellerForm.getBusinessVillageName());
            cs.setString(6, sellerForm.getBusinessVillageAddress());
            cs.setString(7, sellerForm.getProductProductCategory());
            cs.setString(8, sellerForm.getProfileVillagePictureUrl());
            cs.setString(9, sellerForm.getContactPerson());
            cs.setString(10, sellerForm.getContactPhone());
            cs.setString(11, sellerForm.getContactEmail());
            cs.setString(12, sellerForm.getBusinessLicense());
            cs.setString(13, sellerForm.getTaxCode());
            cs.setString(14, sellerForm.getDocumentUrl());
            cs.setString(15, sellerForm.getNote());

            cs.execute();

            int result = cs.getInt(1);

            LOGGER.log(Level.INFO, "sp_InsertSellerVerification_CraftVillage result code: {0}", result);

            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error requesting seller upgrade for sellerID: " + sellerForm.getSellerID(), e);
        } finally {
            closeResources(conn, cs, null);
        }

        return false;
    }

    private SellerVerification mapResultSetToSellerVerification(ResultSet rs) throws SQLException {
        return new SellerVerification(
                rs.getInt("verificationID"),
                rs.getInt("sellerID"),
                rs.getString("businessType"),
                rs.getString("businessVillageCategry"),
                rs.getString("businessVillageName"),
                rs.getString("businessVillageAddress"),
                rs.getString("productProductCategory"),
                rs.getString("profileVillagePictureUrl"),
                rs.getString("contactPerson"),
                rs.getString("contactPhone"),
                rs.getString("contactEmail"),
                rs.getString("idCardNumber"),
                rs.getString("idCardFrontUrl"),
                rs.getString("idCardBackUrl"),
                rs.getString("businessLicense"),
                rs.getString("taxCode"),
                rs.getString("documentUrl"),
                rs.getString("note"),
                rs.getInt("verificationStatus"),
                rs.getInt("verifiedBy"),
                rs.getTimestamp("verifiedDate"),
                rs.getString("rejectReason"),
                rs.getTimestamp("createdDate")
        );
    }

    public List<SellerVerification> getSellerVertificationFormByAdmin(int verificationStatus) {
        String query;
        List<SellerVerification> list = new ArrayList<>();
        if (verificationStatus == 3) {
            query = "SELECT * FROM SellerVerification";
        } else {
            query = "SELECT * FROM SellerVerification WHERE  verificationStatus = ? ";
        }
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            if (verificationStatus != 3) {
                ps.setInt(1, verificationStatus);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSellerVerification(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log tốt hơn
        }
        return list;
    }

    public List<SellerVerification> getSellerVertificationForm(int verificationStatus, int sellerID) {
        String query;
        List<SellerVerification> list = new ArrayList<>();
        if (verificationStatus == 3) {
            query = "SELECT * FROM SellerVerification where sellerID = ?";
        } else {
            query = "SELECT * FROM SellerVerification WHERE  sellerID = ? and verificationStatus = ? ";
        }
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, sellerID);
            if (verificationStatus != 3) {
                ps.setInt(2, verificationStatus);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSellerVerification(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log tốt hơn
        }
        return list;
    }

    public boolean approvedUpgradeAccount(SellerVerification sellerForm) {
        String query = "{? = call sp_ApprovedUpgradeAccount(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = new DBContext().getConnection();
            cs = conn.prepareCall(query);

            // Register the RETURN parameter
            cs.registerOutParameter(1, java.sql.Types.INTEGER);

            // Set input parameters
            cs.setInt(2, sellerForm.getVerificationID());
            cs.setInt(3, sellerForm.getSellerID());
            cs.setString(4, sellerForm.getBusinessVillageCategry());
            cs.setString(5, sellerForm.getBusinessVillageName());
            cs.setString(6, sellerForm.getBusinessVillageAddress());
            cs.setString(7, sellerForm.getProductProductCategory());
            cs.setString(8, sellerForm.getProfileVillagePictureUrl());
            cs.setString(9, sellerForm.getContactPerson());
            cs.setString(10, sellerForm.getContactPhone());
            cs.setString(11, sellerForm.getContactEmail());
            cs.setInt(12, sellerForm.getVerificationStatus());
            cs.setInt(13, sellerForm.getVerifiedBy());

            // Register OUTPUT parameter @newVillageID
            cs.registerOutParameter(14, java.sql.Types.INTEGER);

            cs.execute();

            int result = cs.getInt(1);
            int newVillageId = cs.getInt(14);

            LOGGER.log(Level.INFO, "sp_ApprovedUpgradeAccount result code: {0}", result);
            LOGGER.log(Level.INFO, "New VillageID: {0}", newVillageId);

            return result == 1;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error requesting seller upgrade for sellerID: " + sellerForm.getSellerID(), e);
        } finally {
            closeResources(conn, cs, null);
        }
        return false;
    }

    public boolean rejectedUpgradeAccount(SellerVerification sellerForm) {
        String query = "{? = call sp_RejectedUpgradeAccount(?, ?, ?, ?)}";
        Connection conn = null;
        CallableStatement cs = null;

        try {
            conn = new DBContext().getConnection();
            cs = conn.prepareCall(query);

            // Register the RETURN parameter
            cs.registerOutParameter(1, java.sql.Types.INTEGER);

            // Set input parameters
            cs.setInt(2, sellerForm.getVerificationID());
            cs.setInt(3, sellerForm.getVerificationStatus());
            cs.setInt(4, sellerForm.getVerifiedBy());
            cs.setString(5, sellerForm.getRejectReason());

            cs.execute();

            int result = cs.getInt(1);
            LOGGER.log(Level.INFO, "sp_UpdateSellerVerificationStatus result code: {0}", result);

            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error rejecting seller upgrade for sellerID: " + sellerForm.getSellerID(), e);
        } finally {
            closeResources(conn, cs, null);
        }

        return false;
    }

    public boolean checkPassword(int userId, String password) {
        String query = "SELECT COUNT(*) FROM Account WHERE userID = ? AND password = dbo.HashPassword(?)";
        try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking password for user ID: " + userId, e);
        }
        return false;
    }

    public void updateAccountPassword(int id, String newPassword) {
        // Use SQL Server's HashPassword function to hash the new password
        String query = "UPDATE Account SET password = dbo.HashPassword(?), updatedDate = GETDATE() WHERE userID = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, newPassword); // Hash using SQL Server function
            ps.setInt(2, id);
            ps.executeUpdate();
            LOGGER.log(Level.INFO, "Successfully updated password for account ID: {0}", id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating password for account ID: " + id, e);
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean checkPhoneExists(String phone) {
        return checkPhoneNumberExists(phone);
    }

    // Compatibility methods for UserDAO
    public List<Account> getAccountsWithPaging(int offset, int limit) {
        // Return all accounts (ignore paging for now)
        return getAllAccounts();
    }

    public Account getAccount(String username) {
        // Find account by username
        String query = "SELECT userID, userName, email, fullName, address, phoneNumber, roleID, status, createdDate, updatedDate FROM Account WHERE userName = ? AND status = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting account by username: " + username, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public boolean addAccount(String username, String password, String email, String fullName, String address, String phone, int roleId) {
        Account account = new Account();
        account.setUserName(username);
        account.setPassword(password);
        account.setEmail(email);
        account.setFullName(fullName);
        account.setAddress(address);
        account.setPhoneNumber(phone);
        account.setRoleID(roleId);
        return registerAccount(account);
    }

    public boolean updateAccountStatus(int userId, int status) {
        String query = "UPDATE Account SET status = ? WHERE userID = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, status);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating account status for user ID: " + userId, e);
        } finally {
            closeResources(conn, ps, null);
        }
        return false;
    }

    public boolean updateAccountSimple(Account account) {
        // Don't update username - it's readonly
        String query = "UPDATE Account SET email = ?, fullName = ?, phoneNumber = ?, address = ?, updatedDate = GETDATE() WHERE userID = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, account.getEmail());
            ps.setString(2, account.getFullName());
            ps.setString(3, account.getPhoneNumber());
            ps.setString(4, account.getAddress());
            ps.setInt(5, account.getUserID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }
    
    public static void main(String[] args) {
        System.out.println(new AccountDAO().getAccountById(1));
    }

    
}
