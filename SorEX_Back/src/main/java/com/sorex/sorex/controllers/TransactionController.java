package com.sorex.sorex.controllers;

import com.sorex.sorex.entities.model.Transaction;
import com.sorex.sorex.services.TransactionService;
import com.sorex.sorex.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**Controller relatif aux transactions, il expose des services web REST*/
@RestController
public class TransactionController {

    /**Dépendance aux services des transactions, injectée automatiquement*/
    @Autowired private TransactionService transactionService;
    /**Dépendance aux services des portes monnaie, injectée automatiquement*/
    @Autowired private WalletService walletService;

    /**Méthode pour effectuer un virement vers un autre wallet
     * @param idSrc identifiant du porte monnaie source
     * @param password mot de passe du porte monnaie source
     * @param idDest identifiant du porte monnaie destination
     * @param value montant du virement en question
     * @return La transaction resultante, null en cas d'erreur
     * */
    public Transaction transferSorEx(@RequestParam int idSrc,@RequestParam  String password,@RequestParam  int idDest,
                                     @RequestParam  int value){
        if(!walletService.isExistant(idSrc,password))return null;
        if(walletService.getById(idDest)==null)return null;
        return transactionService.transferSorEx(idSrc,idDest,value);
    }

}
