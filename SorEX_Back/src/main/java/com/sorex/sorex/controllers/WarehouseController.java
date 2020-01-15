package com.sorex.sorex.controllers;
import com.sorex.sorex.entities.model.Item;
import com.sorex.sorex.entities.model.Type;
import com.sorex.sorex.repository.DB;
import com.sorex.sorex.services.ItemService;
import com.sorex.sorex.services.TypeService;
import com.sorex.sorex.services.WalletService;
import com.sorex.sorex.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

/**Controller relatif aux entrepots, il expose des services web en REST*/
@RestController
public class WarehouseController {
    /**Dépendance aux Services des entrepôts, injectée automatiquement*/
    @Autowired private WarehouseService warehouseService;

    /**Dépendance aux Services des portes monnaie injectée automatiquement*/
    @Autowired private WalletService walletService;

    /**Dépendace aux Services des Items injectée automatiquement*/
    @Autowired private ItemService itemService;

    /**Dépendance aux Services des Types injectée automatiquement*/
    @Autowired private TypeService typeService;

    /**Dépendance à la source des données injectée automatiquement*/
    @Autowired private DB db;

    /**Méthode pour mettre à disposition un nouvel Item
     * @param id identifiant du porte monnaie concerné
     * @param password mot de passe du porte monnaie concerné
     * @param idType identifiant du type d'item concerné
     * @param title titre de l'item
     * @param description description textuelle de l'item
     * @param priceByDay prix par jour de l'emprunt de l'item
     * @return true si tout s'est bien passé, false sinon
     * */
    @PostMapping("/warehouse/item/insert")
    public boolean provideItem(@RequestParam int id, @RequestParam String password, @RequestParam int idType,
                               @RequestParam String title, @RequestParam String description, @RequestParam int priceByDay){
        if(!walletService.isExistant(id, password))return false;
        int idItem = itemService.getMaxId()+1;
        Type type;
        if((type=typeService.getById(idType))==null)return false;
        final Item i=Item.builder()
                .dateDeRetour(null)
                .emprunteur(null)
                .idItem(idItem)
                .description(description)
                .prixParJour(priceByDay)
                .proprietaire(walletService.getById(id))
                .title(title)
                .type(type)
                .build();
        if(db.getItemList()==null)if(!db.load())return false;
        db.getItemList().add(i);
        return warehouseService.provideItem(i
                ,walletService.getById(id));
    }
    /**
     * Méthode pour emprunter un item qui a été préalablement créé grace au controller des items
     * @param walletRenterId identifiant du wallet de l'emprunteur
     * @param password mot de passe du wallet de l'emprunteur
     * @param itemId identifiant de l'item prêté
     * @param dateDeRetour date de retour de l'item demandé à être prêté
     * @return true si et seulement si le prêt peut avoir lieu
     */
    @PostMapping("/warehouse/item/rent")
    public boolean rentItem(@RequestParam int walletRenterId, @RequestParam String password,
                            @RequestParam int itemId, @RequestParam String dateDeRetour) {
        if(!walletService.isExistant(walletRenterId, password))return false;
        return warehouseService.rentItem(itemId, walletRenterId, LocalDate.parse(dateDeRetour));
    }

    /**Méthode pour retourner un item
     * @param idWallet identifiant du wallet
     * @param password mot de passe du wallet
     * @param idItem identifiant du wallet
     * */
    @PostMapping("warehouse/item/return")
    public boolean returnItem(@RequestParam int idWallet, @RequestParam String password, @RequestParam int idItem){
        if(!walletService.isExistant(idWallet, password))return false;
        return warehouseService.returnItem(idItem);
    }
}
