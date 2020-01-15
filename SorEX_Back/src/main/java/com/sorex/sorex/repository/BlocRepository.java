package com.sorex.sorex.repository;

import com.sorex.sorex.entities.blockchain.Bloc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**Classe représentant l'entrepôt de données concernant les Blocs*/
@Repository
public class BlocRepository {
    /**Dépendance à la source de données princioale injectée automatiquement*/
    @Autowired private DB db;



    /**Méthode pour récupérer un Bloc par son identifiant
     * @param id identifiant du bloc
     * @return le bloc récupéré, et null si nous ne le trouvons pas
     * */
    public Bloc getById(int id){
        if(db.getBlocList()==null)if(!getData())return null;
        for(Bloc b:db.getBlocList()){
            if(b.getId()==id)return b;
        }
        return null;
    }


    /**Fonction permettant de récupérer les données de leur source
     * @return true     si et seulement si l'operation est réussie, false sinon
     * */
    public boolean getData(){
        if(db.getBlocList()==null)if(!db.load())return false;
        return true;
    }
    /**Méthode pour supprimer tous les blocs
     * @return true si et seulement si tout se passe bien
     * */
    public boolean clearBlocs(){
        if(db.getBlocList()==null)if(!getData())return false;
        db.getBlocList().clear();
        if(!db.save())return false;
        return true;
    }

    /**Méthode pour récupérer l'id maximal des blocs
     * @return l'id maximal, -1 en cas d'erreur*/
    public int maxId(){
        if(db.getBlocList()==null)if(!getData())return -1;
        int i=0;
        for(Bloc b:db.getBlocList()){
            if(i<b.getId())i=b.getId();
        }
        return i;
    }

    public void store(Bloc b){
        if(db.getBlocList()==null)if(!getData())return;
        db.getBlocList().add(b);
        db.save();
        return;
    }
}
