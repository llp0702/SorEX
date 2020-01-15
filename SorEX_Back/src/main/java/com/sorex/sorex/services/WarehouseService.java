package com.sorex.sorex.services;

import com.sorex.sorex.entities.model.Item;
import com.sorex.sorex.entities.model.Transaction;
import com.sorex.sorex.entities.model.Type;
import com.sorex.sorex.entities.model.Wallet;
import com.sorex.sorex.repository.ItemRepository;
import com.sorex.sorex.repository.WalletRepository;
import com.sorex.sorex.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

/**Classe se chargant d'assurer les services d'un entrepôt. Elle manipule l'entrepot de données
 * des entrepots*/
@Service
public class WarehouseService {
    /**Dépendance à l'entrepot de données injectée automatiquement. Permet d'accéder aux données*/
    @Autowired
    private WarehouseRepository warehouseRepository;

    /**Dépendance à l'entrepot de données injectée automatiquement. Permet d'accèder aux données relatives
     * aux Items
     * */
    @Autowired private ItemRepository itemRepository;

    /**Dépendance à l'entrepôt de données injectée automatiquement. Permet d'accèder aux données relatives
     * aux portes-monnaie*/
    @Autowired private WalletRepository walletRepository;

    /**Dépendance aux services des transactions, injectée automatiquement*/
    @Autowired private TransactionService transactionService;

    /**Dépendance aux services des types, injectée automatiquement*/
    @Autowired private TypeService typeService;

    /**Méthode pour qu'un porte monnaie mette à disposition un item
     * @param item item à ajouter dans l'entrepôt
     * @param w wallet du propriétaire de l'item
     * @return true en cas de succès et false sinon*/
    public boolean provideItem(Item item, Wallet w){
        item.setProprietaire(w);
        Type t;
        if((t=item.getType())==null)return false;
        if(t.getBonus()>0){
            return transactionService.transferSorExBonus(w.getIdWallet(),typeService.computeBonus(t.getBonus()),item.getIdItem())!=null;
        }
        return false;
    }

    /**Méthode permettant de vérifier si un item est disponible à la location, et si c'est le cas effectue
     * le prêt
     * @param itemId identifiant de l'objet concerné par le prêt
     * @param idRenter identifiant de l'emprunteur
     * @param dateDeRetour date de retour au format yyyy-MM-dd
     * @return true si et seulement si on arrive à emprunter un objet
     * */
    public boolean rentItem(int itemId, int idRenter, LocalDate dateDeRetour) {
        Item item;
        Wallet walletRenter;
        Transaction t;
        if((item=itemRepository.getById(itemId))==null) return false;
        if((walletRenter=walletRepository.getById(idRenter))==null)return false;
        if(item.getDateDeRetour()!=null)return false;
        if(item.getEmprunteur()!=null)return false;
        long nbDays = dateDeRetour.toEpochDay() - LocalDate.now(ZoneId.systemDefault()).toEpochDay();
        int price = (int)nbDays*item.getPrixParJour();
        if(walletRenter.getBalance()<price)return false;
        item.setEmprunteur(walletRenter);
        item.setDateDeRetour(dateDeRetour);
        if((t=transactionService.transferSorEx(item.getProprietaire().getIdWallet(),
                idRenter, price,itemId))==null)return false;
        if(itemRepository.saveItem(item))return true;
        return false;
    }

    /**Méthode pour qu'un porte-monnaie restitue un item
     * @param itemId identifiant du porte-monnaie du loueur
     * @return true en cas de succès et false sinon
     */
    public boolean returnItem(int itemId) {
        Item i = itemRepository.getById(itemId);
        if(i==null)return false;
        i.setDateDeRetour(null);
        i.setEmprunteur(null);
        return false;
    }
}
