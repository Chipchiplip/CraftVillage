package entity.Account;

public class Account {
    private int userID;
    private String userName;
    private String password;
    private String email;
    private String address;
    private String phoneNumber;
    private int status; // 1: hoạt động, 0: bị chặn
    private String createdDate;
    private String updatedDate;
    private int roleID; // 1: User, 2: Seller, 3: Admin
    private boolean isEmailVerified;
    private String lastLoginDate;
    private int loginAttempts;
    private String lockedUntil; 
    private String avatarUrl;
    private String preferredLanguage; // ví dụ: "vi", "en" ***
    private String fullName;

    // Constructors-------------------------------------------------------------
    public Account() {}

    public Account(String userName, String password, String email, String address, String phoneNumber) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Account(int userID, String userName,String password, String email, String address, String phoneNumber,int roleID, int status, String createdDate, String updatedDate, String fullName) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.fullName = fullName;
    }


    public Account(int userID, String userName, String password, String email, String address, String phoneNumber, int status, int roleID, String fullName) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.roleID = roleID;
        this.fullName = fullName;
    }

    public Account(String userName, String password, String email, String address, String phoneNumber, int status, int roleID, String fullName) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.roleID = roleID;
        this.fullName = fullName;
    }
    
  
    public String getFullName() {
        return fullName;
    }

    //--------------------------------------------------------------------------
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getRole() {
        return roleID;
    }

    public boolean isIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(String lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    @Override
    public String toString() {
        return "Account{" + "userID=" + userID + ", userName=" + userName + ", password=" + password + ", email=" + email + ", address=" + address + ", phoneNumber=" + phoneNumber + ", status=" + status + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", roleID=" + roleID + ", isEmailVerified=" + isEmailVerified + ", lastLoginDate=" + lastLoginDate + ", loginAttempts=" + loginAttempts + ", lockedUntil=" + lockedUntil + ", avatarUrl=" + avatarUrl + ", preferredLanguage=" + preferredLanguage + ", fullName=" + fullName + '}';
    }

    
    
} 