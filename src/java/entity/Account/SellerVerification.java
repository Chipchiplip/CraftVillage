/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.Account;

import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class SellerVerification {
    private int verificationID;
    private int sellerID;
    private String businessType;
    private int businessVillageCategrySelect;
    private String businessVillageCategry;
    private String businessVillageName;
    private String businessVillageAddress;
    private String productProductCategory;
    private int productProductCategorySelect;
    private String profileVillagePictureUrl;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String idCardNumber;
    private String idCardFrontUrl;
    private String idCardBackUrl;
    private String businessLicense;
    private String taxCode;
    private String documentUrl;
    private String note;
    private Integer verificationStatus;
    private Integer verifiedBy;
    private Timestamp verifiedDate;
    private String rejectReason;
    private Timestamp createdDate;

    public SellerVerification() {
    }
// Individual
    public SellerVerification(int sellerID, String businessType, String businessVillageCategry, String businessVillageName, String businessVillageAddress, String productProductCategory, String profileVillagePictureUrl, String contactPerson, String contactPhone, String contactEmail, String idCardNumber, String idCardFrontUrl, String idCardBackUrl, String note) {
        this.sellerID = sellerID;
        this.businessType = businessType;
        this.businessVillageCategry = businessVillageCategry;
        this.businessVillageName = businessVillageName;
        this.businessVillageAddress = businessVillageAddress;
        this.productProductCategory = productProductCategory;
        this.profileVillagePictureUrl = profileVillagePictureUrl;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.idCardNumber = idCardNumber;
        this.idCardFrontUrl = idCardFrontUrl;
        this.idCardBackUrl = idCardBackUrl;
        this.note = note;
    }
     public SellerVerification(int sellerID, String businessType, int businessVillageCategrySelect, String businessVillageName, String businessVillageAddress, int productProductCategorySelect, String profileVillagePictureUrl, String contactPerson, String contactPhone, String contactEmail, String idCardNumber, String idCardFrontUrl, String idCardBackUrl, String note) {
        this.sellerID = sellerID;
        this.businessType = businessType;
        this.businessVillageCategrySelect = businessVillageCategrySelect;
        this.businessVillageName = businessVillageName;
        this.businessVillageAddress = businessVillageAddress;
        this.productProductCategorySelect = productProductCategorySelect;
        this.profileVillagePictureUrl = profileVillagePictureUrl;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.idCardNumber = idCardNumber;
        this.idCardFrontUrl = idCardFrontUrl;
        this.idCardBackUrl = idCardBackUrl;
        this.note = note;
    }
//Craft Village
    public SellerVerification( String businessType,int sellerID, String businessVillageCategry, String businessVillageName, String businessVillageAddress, String productProductCategory, String profileVillagePictureUrl, String contactPerson, String contactPhone, String contactEmail, String businessLicense, String taxCode, String documentUrl, String note) {
        this.sellerID = sellerID;
        this.businessType = businessType;
        this.businessVillageCategry = businessVillageCategry;
        this.businessVillageName = businessVillageName;
        this.businessVillageAddress = businessVillageAddress;
        this.productProductCategory = productProductCategory;
        this.profileVillagePictureUrl = profileVillagePictureUrl;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.businessLicense = businessLicense;
        this.taxCode = taxCode;
        this.documentUrl = documentUrl;
        this.note = note;
    }
    public SellerVerification( String businessType,int sellerID, int businessVillageCategrySelect, String businessVillageName, String businessVillageAddress, int productProductCategorySelect, String profileVillagePictureUrl, String contactPerson, String contactPhone, String contactEmail, String businessLicense, String taxCode, String documentUrl, String note) {
        this.sellerID = sellerID;
        this.businessType = businessType;
        this.businessVillageCategrySelect = businessVillageCategrySelect;
        this.businessVillageName = businessVillageName;
        this.businessVillageAddress = businessVillageAddress;
        this.productProductCategorySelect = productProductCategorySelect;
        this.profileVillagePictureUrl = profileVillagePictureUrl;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.businessLicense = businessLicense;
        this.taxCode = taxCode;
        this.documentUrl = documentUrl;
        this.note = note;
    }
//Get All
    public SellerVerification(int verificationID, int sellerID, String businessType, String businessVillageCategry, String businessVillageName, String businessVillageAddress, String productProductCategory, String profileVillagePictureUrl, String contactPerson, String contactPhone, String contactEmail, String idCardNumber, String idCardFrontUrl, String idCardBackUrl, String businessLicense, String taxCode, String documentUrl, String note, Integer verificationStatus, Integer verifiedBy, Timestamp verifiedDate, String rejectReason, Timestamp createdDate) {
        this.verificationID = verificationID;
        this.sellerID = sellerID;
        this.businessType = businessType;
        this.businessVillageCategry = businessVillageCategry;
        this.businessVillageName = businessVillageName;
        this.businessVillageAddress = businessVillageAddress;
        this.productProductCategory = productProductCategory;
        this.profileVillagePictureUrl = profileVillagePictureUrl;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.idCardNumber = idCardNumber;
        this.idCardFrontUrl = idCardFrontUrl;
        this.idCardBackUrl = idCardBackUrl;
        this.businessLicense = businessLicense;
        this.taxCode = taxCode;
        this.documentUrl = documentUrl;
        this.note = note;
        this.verificationStatus = verificationStatus;
        this.verifiedBy = verifiedBy;
        this.verifiedDate = verifiedDate;
        this.rejectReason = rejectReason;
        this.createdDate = createdDate;
    }

    public SellerVerification(int verificationID, int sellerID, String businessVillageCategry, String businessVillageName, String businessVillageAddress, String productProductCategory, String profileVillagePictureUrl, String contactPerson, String contactPhone, String contactEmail, Integer verificationStatus, Integer verifiedBy) {
        this.verificationID = verificationID;
        this.sellerID = sellerID;
        this.businessVillageCategry = businessVillageCategry;
        this.businessVillageName = businessVillageName;
        this.businessVillageAddress = businessVillageAddress;
        this.productProductCategory = productProductCategory;
        this.profileVillagePictureUrl = profileVillagePictureUrl;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.verificationStatus = verificationStatus;
        this.verifiedBy = verifiedBy;
    }

    public SellerVerification(int verificationID, Integer verificationStatus, Integer verifiedBy, String rejectReason) {
        this.verificationID = verificationID;
        this.verificationStatus = verificationStatus;
        this.verifiedBy = verifiedBy;
        this.rejectReason = rejectReason;
    }
    
   
    
    
    public int getVerificationID() {
        return verificationID;
    }

    public void setVerificationID(int verificationID) {
        this.verificationID = verificationID;
    }

    public int getSellerID() {
        return sellerID;
    }

    public void setSellerID(int sellerID) {
        this.sellerID = sellerID;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }



    public String getBusinessVillageName() {
        return businessVillageName;
    }

    public void setBusinessVillageName(String businessVillageName) {
        this.businessVillageName = businessVillageName;
    }

    public String getBusinessVillageAddress() {
        return businessVillageAddress;
    }

    public void setBusinessVillageAddress(String businessVillageAddress) {
        this.businessVillageAddress = businessVillageAddress;
    }

    public int getBusinessVillageCategrySelect() {
        return businessVillageCategrySelect;
    }

    public void setBusinessVillageCategrySelect(int businessVillageCategrySelect) {
        this.businessVillageCategrySelect = businessVillageCategrySelect;
    }

    public String getBusinessVillageCategry() {
        return businessVillageCategry;
    }

    public void setBusinessVillageCategry(String businessVillageCategry) {
        this.businessVillageCategry = businessVillageCategry;
    }

    public String getProductProductCategory() {
        return productProductCategory;
    }

    public void setProductProductCategory(String productProductCategory) {
        this.productProductCategory = productProductCategory;
    }

    public int getProductProductCategorySelect() {
        return productProductCategorySelect;
    }

    public void setProductProductCategorySelect(int productProductCategorySelect) {
        this.productProductCategorySelect = productProductCategorySelect;
    }
    

    public String getProfileVillagePictureUrl() {
        return profileVillagePictureUrl;
    }

    public void setProfileVillagePictureUrl(String profileVillagePictureUrl) {
        this.profileVillagePictureUrl = profileVillagePictureUrl;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardFrontUrl() {
        return idCardFrontUrl;
    }

    public void setIdCardFrontUrl(String idCardFrontUrl) {
        this.idCardFrontUrl = idCardFrontUrl;
    }

    public String getIdCardBackUrl() {
        return idCardBackUrl;
    }

    public void setIdCardBackUrl(String idCardBackUrl) {
        this.idCardBackUrl = idCardBackUrl;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(int verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public int getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(int verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Timestamp getVerifiedDate() {
        return verifiedDate;
    }

    public void setVerifiedDate(Timestamp verifiedDate) {
        this.verifiedDate = verifiedDate;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "SellerVerification{" + "verificationID=" + verificationID + ", sellerID=" + sellerID + ", businessType=" + businessType + ", businessVillageCategry=" + businessVillageCategry + ", businessVillageName=" + businessVillageName + ", businessVillageAddress=" + businessVillageAddress + ", productProductCategory=" + productProductCategory + ", profileVillagePictureUrl=" + profileVillagePictureUrl + ", contactPerson=" + contactPerson + ", contactPhone=" + contactPhone + ", contactEmail=" + contactEmail + ", idCardNumber=" + idCardNumber + ", idCardFrontUrl=" + idCardFrontUrl + ", idCardBackUrl=" + idCardBackUrl + ", businessLicense=" + businessLicense + ", taxCode=" + taxCode + ", documentUrl=" + documentUrl + ", note=" + note + ", verificationStatus=" + verificationStatus + ", verifiedBy=" + verifiedBy + ", verifiedDate=" + verifiedDate + ", rejectReason=" + rejectReason + ", createdDate=" + createdDate + '}';
    }


}

