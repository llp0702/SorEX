package com.sorex.sorex.repository;

import com.sorex.sorex.entities.blockchain.Bloc;
import com.sorex.sorex.entities.blockchain.Blockchain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**Classe représentant l'entrepôt de données concernant la blockchain*/
@Repository
public class BlockchainRepository{
    /**Dépendance à la source de données princioale injectée automatiquement*/
    @Autowired public DB db;



    /**Fonction permettant de récupérer les données de leur source
     * @return true si et seulement si l'operation est réussie, false sinon
     * */
    public boolean getData(){
        if(db.getBlockchainList()==null)if(!db.load())return false;
        return true;
    }

    /**Méthode pour récupérer une blockchain par son identifiant
     * @param id identifiant de la blockchain
     * @return la blockchain récupérée, et null si nous ne la trouvons pas
     * */
    public Blockchain getById(int id){
        if(db.getBlockchainList()==null)if(!getData())return null;
        for(Blockchain b:db.getBlockchainList()){
            if(b.getId()==id)return b;
        }
        return null;
    }

    /**Méthode pour créer et enregistrer une nouvelle blockchain. Méthode qui ne sera appellée à priori qu'une seule
     * fois. Par conséquent, si on appelle cette méthode et qu'il existe déjà une blockchain dans la liste on la supprime
     * et on la remplace
     * @param difficulty difficulté du minage de la blockchain
     * @return La blockchain créée si cela s'est bien passé, null sinon
     * */
    public Blockchain createAndStore(int difficulty){
        if(db.getBlockchainList()==null)if(!getData())return null;
        if(!db.getBlockchainList().isEmpty())db.getBlockchainList().clear();
        Blockchain blockchain = Blockchain.builder().blocs(new ArrayList<Bloc>()).difficulty(difficulty).id(0).build();
        db.getBlockchainList().add(blockchain);
        if(db.save())return db.getBlockchainList().get(0);
        else return null;
    }

}
