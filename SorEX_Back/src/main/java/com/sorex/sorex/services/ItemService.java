package com.sorex.sorex.services;

import com.sorex.sorex.entities.model.Item;
import com.sorex.sorex.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**Classe se chargant d'assurer les services des Items. Elle manipule l'entrepot de données
 * des items*/
@Service
public class ItemService {
    /**Dépendance à l'entrepot de données injectée automatiquement. Permet d'accéder aux données*/
    @Autowired
    private ItemRepository itemRepository;

    /**Méthode pour récupérer la liste des items relatifs à un porte monnaie dont nous donnons l'identifiant
     * @param id identifiant du porte monnaie concerné
     * @return liste des items liés à l'id donné en paramètres, null en cas d'erreur;
     * */
    public List<Item> getAllById(int id){
        List<Item> result = new ArrayList<Item>();
        for(Item i: itemRepository.getAll()){
            if(i.getProprietaire().getIdWallet()==id)result.add(i);
        }
        return result;
    }

    /**Méthode pour supprimer un item par son identifiant
     * @param idItem identifiant de l'item concerné
     * @return true si et seulement si l'opération s'est bien effectuée
     * */
    public boolean removeById(int idItem){
        if(itemRepository.getById(idItem)==null)return false;
        return itemRepository.removeById(idItem);
    }

    /**Méthode pour récupérer la liste de tous les items
     * @return la liste de tous les items
     * */
    public List<Item> getAll(){
        return itemRepository.getAll();
    }

    /**Méthode pour récupérer l'identifiant maximal des items
     * @return l'identifiant maximal des items, -1 en cas d'erreur*/
    public int getMaxId(){
        return itemRepository.getMaxId();
    }

    /**Méthode pour récupérer un item selon son identifiant
     * @param idItem identifiant de l'item concerné
     * @return l'item concerné, null en cas d'erreur
     * */
    public Item getById(int idItem){
        return itemRepository.getById(idItem);
    }
}
