package service;

import entity.CraftVillage.CraftVillage;
import java.util.List;

public interface ICraftVillageService {
    List<CraftVillage> getAllCraftVillages();
    CraftVillage getCraftVillageById(int id);
    boolean addCraftVillage(CraftVillage craftVillage);
    boolean updateCraftVillage(CraftVillage craftVillage);
    boolean deleteCraftVillage(int id);
} 