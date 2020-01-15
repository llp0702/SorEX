package com.sorex.sorex.repository;

import com.sorex.sorex.entities.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**Classe représentant l'entrepôt de données concernant les Types de services*/
@Repository
public class TypeRepository {
    /**Dépendance à la source de données princioale injectée automatiquement*/
    @Autowired
    private DB db;

    /**Liste contenant les portes monnaie de l'application. Fait office de source de données*/
    private List<Type> list=null;

    /**Fonction permettant de récupérer les données de leur source
     * @return true si et seulement si l'operation est réussie, false sinon
     * */
    public boolean getData(){
        if(db.getTypeList()==null)if(!db.load())return false;
        list = db.getTypeList();
        return true;
    }


    /**Méthode pour récupérer une Tyoe par son identifiant
     * @param id identifiant de la type
     * @return le type récupéré, et null si nous ne le trouvons pas
     * */
    public Type getById(int id){
        if(list==null)if(!getData())return null;
        for(Type t:db.getTypeList())if(t.getIdType()==id)return t;
        return null;
    }

    /**Méthode pour récupérer la liste de tous les types
     * @return la liste de tous les types, null en cas d'erreur*/
    public List<Type> getAll(){
        return db.getTypeList();
    }

}
