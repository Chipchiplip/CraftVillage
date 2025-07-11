package entity.CartWishList;

import java.sql.Timestamp;

public class Wishlist {
    private int wishlistID;
    private int userID;
    private int productID;
    private Timestamp addedDate;
    
    //Contructor----------------------------------------------------------------
    public Wishlist() {
    }

    public Wishlist(int wishlistID, int userID, int productID) {
        this.wishlistID = wishlistID;
        this.userID = userID;
        this.productID = productID;
    }

    
    //--------------------------------------------------------------------------
    public int getWishlistID() {
        return wishlistID;
    }

    public void setWishlistID(int wishlistID) {
        this.wishlistID = wishlistID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public Timestamp getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Timestamp addedDate) {
        this.addedDate = addedDate;
    }

    @Override
    public String toString() {
        return "Wishlist{" + "wishlistID=" + wishlistID + ", userID=" + userID + ", productID=" + productID + ", addedDate=" + addedDate + '}';
    } 
}
