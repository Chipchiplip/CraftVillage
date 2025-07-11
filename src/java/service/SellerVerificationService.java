/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.AccountDAO;
import entity.Account.SellerVerification;
import java.util.List;

/**
 *
 * @author ACER
 */
public class SellerVerificationService implements ISellerVerification {

    AccountDAO aDAO = new AccountDAO();
    @Override
    public boolean requestUpgradeForIndividual(SellerVerification sellerForm) {
        return aDAO.requestUpgradeForIndividual(sellerForm);
    }

    @Override
    public boolean requestUpgradeForCraftVillage(SellerVerification sellerForm) {
        return aDAO.requestUpgradeForCraftVillage(sellerForm);
    }

    @Override
    public List<SellerVerification> getSellerVertificationFormByAdmin(int verificationStatus) {
        return aDAO.getSellerVertificationFormByAdmin(verificationStatus);
    }

    @Override
    public List<SellerVerification> getSellerVertificationForm(int verificationStatus, int sellerID) {
        return aDAO.getSellerVertificationForm(verificationStatus, sellerID);
    }

    @Override
    public boolean approvedUpgradeAccount(SellerVerification sellerForm) {
        return aDAO.approvedUpgradeAccount(sellerForm);
    }

    @Override
    public boolean rejectedUpgradeAccount(SellerVerification sellerForm) {
        return aDAO.rejectedUpgradeAccount(sellerForm);
    }

}

