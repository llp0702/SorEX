package com.sorex.sorex.services;

import com.sorex.sorex.entities.model.Item;
import com.sorex.sorex.entities.model.Transaction;
import com.sorex.sorex.entities.model.Wallet;
import com.sorex.sorex.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**Classe se chargant d'assurer les services d'un porte-monnaie. Elle manipule l'entrepot de données
 * des portes-monnaie*/
@Service
public class WalletService {

    /**Dépendance aux services des Items injectée automatiquement*/
    @Autowired private ItemService itemService;

    /**Dépendance à l'entrepot de connées injectée automatiquement. Permet d'accéder aux données*/
    @Autowired
    private WalletRepository walletRepository;

    /**Méthode permettant de créer un nouveau porte-monnaie avec un mot de passe donné.
     * @param password le mot de passe à attribuer au nouveau porte-monnaie
     * @return -1 en cas d'echec, sinon l'identifiant attribué au nouveau porte-monnaie*/
    public int createNewWallet(String password){
        int id = walletRepository.getMaxId()+1;
        if(walletRepository.storeWallet(Wallet.builder()
                .idWallet(id)
                .balance(0)
                .password(password)
                .transactions(new ArrayList<Transaction>())
                .build()))return id;
        else return -1;
    }

    /**Méthode pour vérifier qu'un porte monnaie existe bien
     * @param id identifiant du porte monnaie
     * @param password mot de passe du porte monnaie
     * @return true si le porte monnaie existe et false sinon*/
    public boolean isExistant(int id, String password){
        return walletRepository.isExistant(id, password);
    }

    /**Méthode pour récupérer le solde d'un porte monnaie par id
     * @param id identifiant du porte monnaie
     * @return solde du porte monnaie en question, -1 en cas d'erreur*/
    public int getBalanceById(int id){
        Wallet w;
        if((w=walletRepository.getById(id))==null)return -1;
        return w.getBalance();
    }

    /**Méthode pour récupérer la liste des transactions par identifiant du porte monnaie
     * @param id identifiant du porte monnaie concerné
     * @return Liste des transactions du porte monnaie trouvé, null en cas d'erreur
     * */
    public List<Transaction> getTransactionsById(int id){
        Wallet w;
        if((w=walletRepository.getById(id))==null)return null;
        return w.getTransactions();
    }

    /**Méthode pour récupérer un porte monnaie par son identifiant
     * @param id identifiant du porte monnaie
     * @return Le porte monnaie concerné, null en cas d'erreur
     */
    public Wallet getById(int id){
        return walletRepository.getById(id);
    }

    /**Méthode pour supprimer un porte monnaie par son identifiant
     *  La suppression ne peut pas avoir lieu si un des items du porte monnaie
     *  est actuellement loué. La suppression n'a également pas lieu si le porte monnaie
     *  loue actuellement des items.
     *  @param idWallet identifiant du porte monnaie concerné
     *  @return true si et seulement si l'opération s'est déroulée avec succés
     * */
    public boolean removeWalletById(int idWallet){
        Wallet w;
        if((w = getById(idWallet))==null)return false;
        for(Item i:itemService.getAllById(idWallet))
            if(i.getEmprunteur() != null || i.getDateDeRetour() != null)return false;
        for(Item i:itemService.getAll())if(i.getEmprunteur().getIdWallet()==idWallet)return false;
        for(Transaction t:w.getTransactions()){
            if(t.getSrc().getIdWallet()==idWallet)t.setSrc(null);
            else if(t.getDest().getIdWallet()==idWallet)t.setDest(null);
        }
        for(Item i:itemService.getAllById(idWallet))if(!itemService.removeById(i.getIdItem())){
            return false;
        }
        return walletRepository.removeById(idWallet);
    }
}
