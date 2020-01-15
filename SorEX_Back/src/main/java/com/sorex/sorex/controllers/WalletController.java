package com.sorex.sorex.controllers;

import com.sorex.sorex.entities.model.Transaction;
import com.sorex.sorex.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**Controller relatif aux porte-monnaie, il expose des services web en REST*/
@RestController
public class WalletController {
    /**Dépendance aux services relatifs aux portes-monnaie, cette dépendance est injectée automatiquement */
    @Autowired private WalletService walletService;

    /**Méthode pour créer un nouveau wallet à partir d'un mot de passe
     * @param password Mot de passe associé au nouveau porte-monnaie créé
     * @return l'identifiant créé pour ce porte-monnaie, -1 en cas d'échec  */
    @GetMapping(value= "/wallet/create")
    public int createWallet(@RequestParam String password){
        return walletService.createNewWallet(password);
    }

    /**Méthode pour consulter son solde à partir de l'identifiant ainsi que le mot de passe d'un porte-monnaie
     * @param id identifiant du porte monnaie
     * @param password mot de passe du porte monnaie
     * @return le solde du porte monnaie, -1 en cas d'echec*/
    @GetMapping(value="/wallet/balance/get")
    public int consultBalance(@RequestParam int id, @RequestParam String password){
        if(!walletService.isExistant(id,password))return -1;
        return walletService.getBalanceById(id);
    }

    /**Méthode pour récupérer les transations relatives à un porte monnaie dont nous donnons l'identifiant et le password
     * @param id identifiant du porte monnaie
     * @param password mot de passe du porte monnaie
     * @return liste des transactions récupérées, null en cas d'erreur*/
    @GetMapping(value="/wallet/transactions/list")
    public List<Transaction> getTransactionsById(@RequestParam int id, @RequestParam String password){
        if(!walletService.isExistant(id,password))return null;
        return walletService.getTransactionsById(id);
    }

    /**Méthode pour supprimer un porte-monnaie, les biens possédés par ce porte-monnaie seront
     * supprimés. La suppression ne peut pas avoir lieu si un des items du porte monnaie
     * est actuellement loué. La suppression n'a également pas lieu si le porte monnaie
     * loue actuellement des items.
     * @param idWallet identifiant du porte monnaie concerné
     * @param password mot de passe du porte monnaie concerné
     * @return true si et seulement si l'opération s'est bien déroulée
     * */
    @PostMapping(value="/wallet/remove")
    public boolean removeWallet(@RequestParam int idWallet, @RequestParam String password){
        if(!walletService.isExistant(idWallet,password))return false;
        return walletService.removeWalletById(idWallet);
    }

    /**Méthode pour récupérer la liste des transactions par utilisateur
     * @param idWallet identifiant du porte monnaie concerné
     * @param password mot de passe du porte monnaie concerné
     * @return la liste des transactions de l'utilisateur concerné, null en cas d'erreur
     */
    @GetMapping(value = "/wallet/transaction/get/all")
    public List<Transaction> getAllTransactions(@RequestParam int idWallet, @RequestParam String password){
        System.out.println("hello hello");
        if(!walletService.isExistant(idWallet,password))return null;
        return walletService.getTransactionsById(idWallet);
    }

}
