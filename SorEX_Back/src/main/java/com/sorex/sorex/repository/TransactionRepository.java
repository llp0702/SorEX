package com.sorex.sorex.repository;

import com.sorex.sorex.entities.model.Transaction;
import com.sorex.sorex.services.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**Classe représentant l'entrepôt de données concernant les transactions*/
@Repository
public class TransactionRepository {

    /**Dépendance à la source de données princioale injectée automatiquement*/
    @Autowired private DB db;

    /**Dépendance au service blockchain*/
    @Autowired private BlockchainService blockchainService;

    /**Fonction permettant de récupérer les données de leur source
     * @return true si et seulement si l'operation est réussie, false sinon
     * */
    public boolean getData(){
        if(db.getTransactionList()==null)if(!db.load())return false;
        return true;
    }

    /**Méthode pour récupérer une transaction par son identifiant
     * @param id identifiant de la transaction
     * @return la transaction récupérée, et null si nous ne la trouvons pas
     * */
    public Transaction getById(int id){
        if(db.getTransactionList()==null)if(!getData())return null;
        for(Transaction t : db.getTransactionList()){
            if(t.getIdTransaction() == id)return t;
        }return null;
    }

    /**Méthode pour récupérer l'identifiant maximal
     * @return id récupéré, -1 en cas d'erreur*/
    public int getIdMax(){
        if(db.getTransactionList()==null)if(!getData())return -1;
        int i=0;
        for(Transaction t:db.getTransactionList()){
            if(t.getIdTransaction()>i)i=t.getIdTransaction();
        }return i;
    }
    /**Méthode pour ajouter une nouvelle transaction
     * @return true si et seulement si tout s'est bien passé*/
    public boolean addNewTransaction(Transaction t){
        if(db.getTransactionList()==null)if(!getData())return false;
        db.getTransactionList().add(t);
        blockchainService.addTransaction(t);
            return db.save();
    }
}
