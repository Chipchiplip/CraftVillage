/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity.CraftVillage;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author ACER
 */
public class CraftFavorite {
    private int favoriteID;
    private int userID;
    private int villageID;
    private Timestamp addedDate;

    // Constructors-------------------------------------------------------------
    public CraftFavorite() {
    }
    
    //--------------------------------------------------------------------------
    public int getFavoriteID() {
        return favoriteID;
    }

    public void setFavoriteID(int favoriteID) {
        this.favoriteID = favoriteID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getVillageID() {
        return villageID;
    }

    public void setVillageID(int villageID) {
        this.villageID = villageID;
    }

    public Timestamp getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Timestamp addedDate) {
        this.addedDate = addedDate;
    }

    @Override
    public String toString() {
        return "CraftFavorite{" + "favoriteID=" + favoriteID + ", userID=" + userID + ", villageID=" + villageID + ", addedDate=" + addedDate + '}';
    }
    
}
