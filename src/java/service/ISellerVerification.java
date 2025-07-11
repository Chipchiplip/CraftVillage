/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import entity.Account.SellerVerification;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface ISellerVerification {
    boolean requestUpgradeForIndividual(SellerVerification sellerForm);
    boolean requestUpgradeForCraftVillage(SellerVerification sellerForm);
    List<SellerVerification> getSellerVertificationFormByAdmin(int verificationStatus);
    List<SellerVerification> getSellerVertificationForm(int verificationStatus, int sellerID);
    boolean approvedUpgradeAccount(SellerVerification sellerForm);
    boolean rejectedUpgradeAccount(SellerVerification sellerForm);
}

