/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import entity.CraftVillage.CraftReview;
import entity.CraftVillage.CraftType;
import entity.CraftVillage.CraftVillage;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface IVillageService {
    int addVillage(CraftVillage village) throws Exception;
    boolean updateVillage(CraftVillage village);
    boolean deleteVillage(int villageId);
    CraftVillage getVillageById(int villageId);
    List<CraftVillage> getAllVillages();
    List<CraftVillage> searchVillages(String keyword);
    List<CraftVillage> getVillagesBySeller(int sellerId);
    List<CraftVillage> getTopVisitedVillages(int limit);
    List<CraftType> getAllCraftType();
    String getVillageNameByID (int villageID);
    String getCraftTypeNameByID (int typeID);
    boolean updateCraftVillageByAdmin(CraftVillage village);
    List<CraftVillage> getAllCraftVillageActive();
    boolean deleteVillageByAdmin(int villageID);
    boolean addNewVillageByAdmin(CraftVillage village);
    List<CraftVillage> getSearchVillageByAdmin(int status, int searchID, String contentSearch);
    String getVillageNameByTypeID(Integer typeID) ;
    List<CraftVillage> getTopRatedByAdmin();
    List<CraftVillage> getVillageByCategory(int typeID);
}
