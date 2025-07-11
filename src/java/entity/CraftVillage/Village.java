package entity.CraftVillage;

import java.sql.Timestamp;

public class Village {
    private int villageId;
    private String name;
    private String description;
    private String imageUrl;
    private int districtId;
    private String districtName;
    private int status; // 1: Active, 0: Inactive
    private Timestamp createdDate;
    private Timestamp updatedDate;

    public Village() {
        this.status = 1; // Active by default
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public Village(int villageId, String name, String description, String imageUrl, 
                  int districtId, String districtName, int status,
                  Timestamp createdDate, Timestamp updatedDate) {
        this.villageId = villageId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.districtId = districtId;
        this.districtName = districtName;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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

    public boolean isActive() {
        return status == 1;
    }

    public void setActive(boolean active) {
        this.status = active ? 1 : 0;
    }

    @Override
    public String toString() {
        return "Village{" +
                "villageId=" + villageId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", districtId=" + districtId +
                ", districtName='" + districtName + '\'' +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
} 