package com.sorex.sorex.controllers;

import com.sorex.sorex.entities.model.Item;
import com.sorex.sorex.services.ItemService;
import com.sorex.sorex.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**Controller relatif aux Items, il expose des services web en REST*/
@RestController
public class ItemController {
    /**Dépendance aux services relatifs aux Items, cette dépendance est injectée automatiquement */
    @Autowired private ItemService itemService;
    /**Dépendance aux services relatifs aux portes-monnaie, cette dépendance est injectée automatiquement */
    @Autowired private WalletService walletService;

    /**Méthode pour récupérer tous les items relatifs à un porte monnaie
     * @param id identifiant du wallet concerné
     * @param password mot de passe du wallet concerné
     * @return la liste des items récupérés, null en cas d'erreur
     */
    @GetMapping(value="/item/list/byId")
    public List<Item> getAllById(@RequestParam int id, @RequestParam String password){
        if(!walletService.isExistant(id,password))return null;
        return itemService.getAllById(id);
    }

    /**Méthode pour supprimer un item selon son id
     * @param idWallet identifiant du wallet du propriétaire
     * @param password mot de passe du wallet du propriétaire
     * @param id identifiant de l'item
     * @return true si ça s'est bien passé
     * */
    @GetMapping(value="/items/remove/id")
    public boolean removeById(@RequestParam int idWallet,@RequestParam  String password,@RequestParam  int id){
        if(!walletService.isExistant(idWallet,password))return false;
        Item i = itemService.getById(id);
        if(i==null)return false;
        if(i.getProprietaire().getIdWallet()!=idWallet)return false;
        return itemService.removeById(idWallet);
    }

    /**Méthode pour récupérer tous les items de l'application
     * @return la liste des items de l'application, null en cas d'erreur
     * */
    @GetMapping(value = "/items/get/all")
    public List<Item> getAll(){
        List<Item>result = itemService.getAll();
        System.out.println(result);
        return result  ;
    }

}

