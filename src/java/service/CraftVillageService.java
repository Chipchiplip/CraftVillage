package service;

import entity.CraftVillage.CraftVillage;
import java.util.List;
import DAO.CraftVillageDAO;

public class CraftVillageService implements ICraftVillageService {
    private CraftVillageDAO craftVillageDAO = new CraftVillageDAO();

    @Override
    public List<CraftVillage> getAllCraftVillages() {
        try {
            return craftVillageDAO.getAllVillages();
        } catch (Exception e) {
            // Return empty list instead of null to prevent errors
            return new java.util.ArrayList<>();
        }
    }

    @Override
    public CraftVillage getCraftVillageById(int id) {
        // TODO: implement
        return null;
    }

    @Override
    public boolean addCraftVillage(CraftVillage craftVillage) {
        // TODO: implement
        return false;
    }

    @Override
    public boolean updateCraftVillage(CraftVillage craftVillage) {
        // TODO: implement
        return false;
    }

    @Override
    public boolean deleteCraftVillage(int id) {
        // TODO: implement
        return false;
    }
} 