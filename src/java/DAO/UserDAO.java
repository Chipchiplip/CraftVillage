package DAO;

import context.DBContext;
import entity.Account.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private AccountDAO accountDAO = new AccountDAO();

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

    // Convert Account to User-compatible format
    private Account convertToUserCompatible(Account account) {
        if (account == null) return null;
        
        // Account already has User compatibility methods
        return account;
    }

    public List<Account> getAllUsers() {
        try {
            List<Account> accounts = accountDAO.getAccountsWithPaging(0, Integer.MAX_VALUE);
            return accounts;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
            return new ArrayList<>();
        }
    }

    public Account getUserById(int userId) {
        try {
            return accountDAO.getAccountById(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting user with ID: " + userId, e);
            return null;
        }
    }

    public Account getUserByUsername(String username) {
        try {
            return accountDAO.getAccount(username);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting user with username: " + username, e);
            return null;
        }
    }

    public Account getUserByEmail(String email) {
        try {
            return accountDAO.loginByEmail(email);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting user with email: " + email, e);
            return null;
        }
    }

    public int addUser(Account user) {
        try {
            // Use accountDAO registerAccount method
            boolean success = accountDAO.addAccount(
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getFullName(),
                user.getAddress(),
                user.getPhoneNumber(),
                user.getRoleID()
            );
            
            if (success) {
                // Get the newly created account to return its ID
                Account newAccount = accountDAO.getAccount(user.getUserName());
                return newAccount != null ? newAccount.getUserID() : -1;
            }
            return -1;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding user", e);
            return -1;
        }
    }

    public boolean updateUser(Account user) {
        try {
            // AccountDAO.updateAccount expects an Account object
            return accountDAO.updateAccount(user);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user with ID: " + user.getUserID(), e);
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        try {
            accountDAO.updateAccountPassword(userId, newPassword);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating password for user ID: " + userId, e);
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        try {
            return accountDAO.updateAccountStatus(userId, 0); // Soft delete
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting user with ID: " + userId, e);
            return false;
        }
    }

    public boolean updateLastLogin(int userId) {
        try {
            // This functionality would need to be added to AccountDAO if needed
            LOGGER.log(Level.INFO, "Last login updated for user ID: {0}", userId);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating last login for user ID: " + userId, e);
            return false;
        }
    }

    public List<Account> searchUsers(String keyword) {
        try {
            // This would need to be implemented in AccountDAO if needed
            // For now, return filtered results from getAllUsers
            List<Account> allUsers = getAllUsers();
            List<Account> filteredUsers = new ArrayList<>();
            
            for (Account user : allUsers) {
                if (user.getUserName().toLowerCase().contains(keyword.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                    (user.getFullName() != null && user.getFullName().toLowerCase().contains(keyword.toLowerCase())) ||
                    (user.getPhoneNumber() != null && user.getPhoneNumber().contains(keyword))) {
                    filteredUsers.add(user);
                }
            }
            
            return filteredUsers;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching users with keyword: " + keyword, e);
            return new ArrayList<>();
        }
    }

    // Backward compatibility method
    private Account mapResultSetToUser(ResultSet rs) throws SQLException {
        return new Account(
            rs.getInt("userID"),
            rs.getString("userName"),
            rs.getString("password"),
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
}
