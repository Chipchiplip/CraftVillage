package entity.Product;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {

    private int pid;
    private String name;
    private BigDecimal price;
    private String description;
    private int stock;
    private int stockAdd;
    private int status;
    private int villageID;
    private int categoryID;
    private String mainImageUrl;
    private int clickCount;
    private Timestamp lastClicked;
    private Timestamp createdDate;
    private Timestamp updatedDate;
    private int craftTypeID;
    private String sku;
    private BigDecimal weight;
    private String dimensions;
    private String materials;
    private String careInstructions;
    private String warranty;
    private boolean isFeatured;
    private BigDecimal averageRating;
    private int totalReviews;

    // Constructors-------------------------------------------------------------
    public Product() {
    }

    public Product(int pid,String name, BigDecimal price, String description, int stock, int status, int villageID, int categoryID,int craftTypeID, String mainImageUrl, int clickCount, Timestamp lastClicked, Timestamp createdDate, Timestamp updatedDate, String sku, BigDecimal weight, String dimensions, String materials, String careInstructions, String warranty, BigDecimal averageRating, int totalReviews) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.status = status;
        this.villageID = villageID;
        this.categoryID = categoryID;
        this.craftTypeID = craftTypeID;
        this.mainImageUrl = mainImageUrl;
        this.clickCount = clickCount;
        this.lastClicked = lastClicked;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.sku = sku;
        this.weight = weight;
        this.dimensions = dimensions;
        this.materials = materials;
        this.careInstructions = careInstructions;
        this.warranty = warranty;
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
    }
    
    public Product(int pid, String name, BigDecimal price, String description, int stock, int status, int villageID, int categoryID, String mainImageUrl, int clickCount, Timestamp lastClicked, Timestamp createdDate, Timestamp updatedDate) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.status = status;
        this.villageID = villageID;
        this.categoryID = categoryID;
        this.mainImageUrl = mainImageUrl;
        this.clickCount = clickCount;
        this.lastClicked = lastClicked;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Product(int pid, String name, BigDecimal price, String description, int stock, int stockAdd, int status, int villageID, int categoryID, String mainImageUrl, int craftTypeID, String sku, BigDecimal weight, String dimensions, String materials, String careInstructions, String warranty) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.stockAdd = stockAdd;
        this.status = status;
        this.villageID = villageID;
        this.categoryID = categoryID;
        this.mainImageUrl = mainImageUrl;
        this.craftTypeID = craftTypeID;
        this.sku = sku;
        this.weight = weight;
        this.dimensions = dimensions;
        this.materials = materials;
        this.careInstructions = careInstructions;
        this.warranty = warranty;
    }

    public Product(String name, BigDecimal price, String description, int stock, int status, int villageID, int categoryID, String mainImageUrl, int craftTypeID, String sku, BigDecimal weight, String dimensions, String materials, String careInstructions, String warranty) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.status = status;
        this.villageID = villageID;
        this.categoryID = categoryID;
        this.mainImageUrl = mainImageUrl;
        this.craftTypeID = craftTypeID;
        this.sku = sku;
        this.weight = weight;
        this.dimensions = dimensions;
        this.materials = materials;
        this.careInstructions = careInstructions;
        this.warranty = warranty;
    }
  
    
    
    //--------------------------------------------------------------------------
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVillageID() {
        return villageID;
    }

    public void setVillageID(int villageID) {
        this.villageID = villageID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public Timestamp getLastClicked() {
        return lastClicked;
    }

    public void setLastClicked(Timestamp lastClicked) {
        this.lastClicked = lastClicked;
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

    public int getCraftTypeID() {
        return craftTypeID;
    }

    public void setCraftTypeID(int craftTypeID) {
        this.craftTypeID = craftTypeID;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public boolean isIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int totalReviews) {
        this.totalReviews = totalReviews;
    }

    // Alias methods for JSP compatibility
    public int getId() {
        return pid;
    }

    public void setId(int id) {
        this.pid = id;
    }

    // Alias method for JSP compatibility
    public int getCateID() {
        return categoryID;
    }

    public void setCateID(int cateID) {
        this.categoryID = cateID;
    }

    public String getImg() {
        return mainImageUrl;
    }

    public void setImg(String img) {
        this.mainImageUrl = img;
    }

    @Override
    public String toString() {
        return "Product{" + "pid=" + pid + ", name=" + name + ", price=" + price + ", description=" + description + ", stock=" + stock + ", status=" + status + ", villageID=" + villageID + ", categoryID=" + categoryID + ", mainImageUrl=" + mainImageUrl + ", clickCount=" + clickCount + ", lastClicked=" + lastClicked + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", craftTypeID=" + craftTypeID + ", sku=" + sku + ", weight=" + weight + ", dimensions=" + dimensions + ", materials=" + materials + ", careInstructions=" + careInstructions + ", warranty=" + warranty + ", isFeatured=" + isFeatured + ", averageRating=" + averageRating + ", totalReviews=" + totalReviews + '}';
    }

    public int getStockAdd() {
        return stockAdd;
    }

    public void setStockAdd(int stockAdd) {
        this.stockAdd = stockAdd;
    }

    
}
