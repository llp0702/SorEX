package com.sorex.sorex.repository;


import com.sorex.sorex.entities.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**Classe représentant l'entrepôt de données concernant les portes monnaies*/
@Repository
public class WalletRepository {
    /**Dépendance à la source de données princioale injectée automatiquement*/
    @Autowired private DB db;


    /**Fonction permettant de récupérer les données de leur source
     * @return true si et seulement si l'operation est réussie, false sinon
     * */
    public boolean getData(){
        if(db.getWalletList()==null)if(!db.load())return false;
        return true;
    }

    /**Méthode pour récupérer une porte-monnaie par son identifiant
     * @param id identifiant de la transaction
     * @return la transaction récupérée, et null si nous ne la trouvons pas
     * */
    public Wallet getById(int id) {
        if(db.getWalletList()==null)getData();
        for(Wallet w : db.getWalletList()){
            if(w.getIdWallet()==id)return w;
        }return null;
    }

    /**Méthode pour récupérer l'identifiant maximal parmi tous les wallet
     * @return l'identifiant maximal des Wallet
     * */
    public int getMaxId(){
        int i=0;
        if(db.getWalletList()==null)if(!getData())return 0;
        for(Wallet w:db.getWalletList()){
            if(w.getIdWallet()>i)i=w.getIdWallet();
        }
        return i;
    }

    /**Méthode permettant de stocker un nouveau wallet dans le repository.
     * pour stocker définitivement les données on appelle la méthode save() de DB.
     * @param w le wallet à ajouter
     * @return true si l'opération s'est bien effectuée, false sinon
     * */
    public boolean storeWallet(Wallet w){
        if(db.getWalletList()==null)if(!getData())return false;
        db.getWalletList().add(w);
        db.save();
        return true;
    }

    /**Méthode permettant de vérifier si un porte monnaie existe selon l'identifiant et le mot de passe donné en paramètre
     * @param id identifiant du porte monnaie recherché
     * @param password mot de passe du porte monnaie recherché
     * @return true si et seulement si le porte monnaie a été retrouvé, false sinon*/
    public boolean isExistant(int id, String password){
        if(db.getWalletList()==null)if(!getData())return false;
        for(Wallet w : db.getWalletList())if(w.getIdWallet()==id && password.equals(w.getPassword()))return true;
        return false;
    }

    /**Méthode pour supprimer un wallet selon son identifiant
     * @param idWallet identifiant du porte monnaie concerné
     * @return true si et seulement si l'opération s'est bien déroulée
     * */
    public boolean removeById(int idWallet){
        if(db.getWalletList()==null)if(!getData())return false;
        Wallet w;
        if((w=getById(idWallet))==null)return false;
        db.getWalletList().remove(w);
        if(db.save())return true;
        else return false;
    }
}

