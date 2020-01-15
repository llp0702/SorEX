package com.sorex.sorex.repository;

import com.sorex.sorex.entities.model.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**Classe représentant l'entrepôt de données concernant les entrepots*/
@Repository
public class WarehouseRepository {
    /**Dépendance à la source de données princioale injectée automatiquement*/
    @Autowired private DB db;

    /**Liste contenant les entrepots de l'application. Fait office de source de données*/
    private List<Warehouse> list=null;

    /**Méthode pour récupérer un entrepot par id
     * @param id identifiant de l'entrepot
     * @return l'entrepot correspondant à l'entifiant donné en paramètre
     * */
    public Warehouse getById(int id){
        if(list==null)if(!getData())return null;
        for(Warehouse e:list)if(e.getIdWarehouse()==id)return e;
        return null;
    }

    /**Fonction permettant de récupérer les données de leur source
     * @return true si et seulement si l'operation est réussie, false sinon
     * */
    public boolean getData(){
        if(db.getWarehouseList()==null)if(!db.load())return false;
        list = db.getWarehouseList();
        return true;
    }
}
