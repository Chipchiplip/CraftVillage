package entity.CraftVillage;

import java.sql.Timestamp;

public class CraftType {
    private int typeID;
    private String typeName;
    private String description;
    private int status; // 1: hoạt động, 0: ẩn
    private Timestamp createdDate;
    private Timestamp updatedDate;

    // Constructors-------------------------------------------------------------
    public CraftType() {}

    public CraftType(int typeID, String typeName, String description, int status, Timestamp createdDate, Timestamp updatedDate) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public CraftType(int typeID, String typeName, String description) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.description = description;
    }
    
    //--------------------------------------------------------------------------
    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    // Alias methods for compatibility with DAO
    public int getCraftTypeID() {
        return typeID;
    }

    public void setCraftTypeID(int craftTypeID) {
        this.typeID = craftTypeID;
    }

    public String getCraftTypeName() {
        return typeName;
    }

    public void setCraftTypeName(String craftTypeName) {
        this.typeName = craftTypeName;
    }

    @Override
    public String toString() {
        return "CraftType{" + "typeID=" + typeID + ", typeName=" + typeName + ", description=" + description + ", status=" + status + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }
} 