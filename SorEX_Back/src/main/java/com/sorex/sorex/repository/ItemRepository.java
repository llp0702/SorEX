package com.sorex.sorex.repository;

import com.sorex.sorex.entities.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**Classe représentant l'entrepôt de données concernant les items*/
@Repository
public class ItemRepository {
    /**Dépendance à la source de données princioale injectée automatiquement*/
    @Autowired private DB db;

    /**Méthode pour récupérer un item par id
     * @param id identifiant de l'item
     * @return l'item correspondant à l'identifiant donné en paramètre
     * */
    public Item getById(int id){
        if(db.getItemList()==null)getData();
        for(Item i:db.getItemList()){
            if(i.getIdItem()==id)return i;
        }return null;
    }

    /**Méthode permettant de récupérer les données de leur source
     * @return true si et seulement si l'operation est réussie, false sinon
     * */
    public boolean getData(){
        if(db.getItemList()==null)if(!db.load())return false;
        return true;
    }

    /**Méthode pour récupérer tous les items de l'application
     * @return la liste des items de l'application, null en cas d'erreur
     * */
    public List<Item> getAll(){
        if(db.getItemList()==null)getData();
        return db.getItemList();
    }

    /**Méthode pour enregistrer un nouvel Item
     * @param item nouvel objet item à enregistrer
     * @return true en cas de réussite et false sinon
     * */
    public boolean saveItem(Item item){
        if(db.getItemList()==null)if(!db.load())return false;
        db.getItemList().add(item);
        db.save();
        return true;
    }

    /**Méthode pour récupérer l'id maximal
     * @return l'identidiant maximal des items, -1 en cas d'erreur*/
    public int getMaxId(){
        if(db.getItemList()==null)if(!getData())return -1;
        int i=0;
        for(Item item : db.getItemList())if(item.getIdItem()>i)i=item.getIdItem();
        return i;
    }

    /**Méthode pour supprimer un item par son id
     * @param id identifiant de l'item
     * @return true si et seulement si l'opération s'est bien passée
     * */
    public boolean removeById(int id){
        if(db.getItemList()==null)if(!getData())return false;
        for(Item i:db.getItemList())if(i.getIdItem()==id){
            db.getItemList().remove(i);
            if(db.save())return true;
            else return false;
        }
        return false;
    }

}
