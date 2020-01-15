package com.sorex.sorex.services;


import com.sorex.sorex.entities.blockchain.Bloc;
import com.sorex.sorex.entities.blockchain.Blockchain;
import com.sorex.sorex.entities.model.Transaction;
import com.sorex.sorex.repository.BlockchainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**Classe assurant les services liés à la Blockchain*/
@Service
public class BlockchainService {
    /**Dépendance aux services des Blocs, injectée automatiquement */
    @Autowired private BlocService blocService;

    /**Dépendance à l'entrepôt de données de la blockchain, injectée automatiquement*/
    @Autowired private BlockchainRepository blockchainRepository;

    /**Méthode pour créer une blockchain. Cette fonction est sensée être appellée qu'une seule fois.*/
    public boolean createBlockchain(int difficulty){
        if(!blocService.clearAllBlocs())return false;
        Blockchain b =  blockchainRepository.createAndStore(4);
        Bloc firstBloc = Bloc.builder().data(null).id(0)
                .hash(null).nonce(1).index(0).previousHash(null).timestamp(System.currentTimeMillis()).build();
        blocService.store(firstBloc);
        blocService.mineBloc(0,b.getDifficulty());
        b.getBlocs().add(firstBloc);
        if(!blockchainRepository.db.save())return false;
        else return true;
    }

    /**
     * Method that adds a block to our Block chain after mining Block
     * by resolving the Proof of Work
     *
     * @param b Bloc à ajouter
     * @return true si tout va bien, false sinon
     */
    public boolean addBloc(Bloc b) {
        if(blockchainRepository.db.getBlockchainList()==null)blockchainRepository.getData();
        if(blockchainRepository.getById(0)==null)createBlockchain(4);
        Blockchain blockchain = blockchainRepository.getById(0);
        if (b != null) {
            blocService.store(b);
            if(!blocService.mineBloc(b.getId(),blockchain.getDifficulty()))return false;
            blockchain.getBlocs().add(b);
            return true;
        }return false;
    }

    /**Méthode qui vérifie si le 1er bloc de la blockchain est valide
     * @return true si et seulement si le 1er bloc est valide*/
    public boolean isFirstBlockValid() {
        if(blockchainRepository.db.getBlockchainList()==null)blockchainRepository.getData();
        if(blockchainRepository.getById(0)==null)createBlockchain(4);
        Blockchain blockchain = blockchainRepository.getById(0);
        if(blockchain==null)return false;
        Bloc firstBloc = blockchain.getBlocs().get(0);

        if (firstBloc.getIndex() != 0) {
            return false;
        }

        if (firstBloc.getPreviousHash() != null) {
            return false;
        }

        if (firstBloc.getHash() == null || !blocService.calculateHash(firstBloc).equals(firstBloc.getHash())) {
            return false;
        }

        return true;
    }

    /**
     * Method that verifies that a Block is valid
     * by comparing its previousHash to it's predecessor's hash
     * @param bloc  bloc à vérifier
     * @param previousBloc bloc précédent
     * @return true or false
     */
    public boolean isValidNewBlock(Bloc bloc, Bloc previousBloc) {
        if (bloc != null && previousBloc != null) {
            if ((previousBloc.getIndex() + 1) != bloc.getIndex()) {
                return false;
            }

            if (bloc.getPreviousHash() == null || !bloc.getPreviousHash().equals(previousBloc.getHash())) {
                return false;
            }

            if (bloc.getHash() == null || !blocService.calculateHash(bloc).equals(bloc.getHash())) {
                return false;
            }

            return true;
        }

        return false;
    }
    /**Méthode pour vérifier si la blockchain est valide
     * @return true si et seulement si la blockchain est valide
     * */
    public boolean isBlockChainValid() {
        if(blockchainRepository.db.getBlockchainList()==null)blockchainRepository.getData();
        if(blockchainRepository.getById(0)==null)createBlockchain(4);
        Blockchain blockchain = blockchainRepository.getById(0);
        if (!isFirstBlockValid()) {
            return false;
        }
        for (int i = 1; i < blockchain.getBlocs().size(); i++) {
            Bloc currentBloc = blockchain.getBlocs().get(i);
            Bloc previousBloc = blockchain.getBlocs().get(i - 1);

            if(!isValidNewBlock(currentBloc, previousBloc)) {
                return false;
            }
        }

        return true;
    }

    /**Méthode pour ajouter une transaction dans la blockchain, en créant un bloc et en l'ajoutant
     * @param t la transaction concernée
     * @return true si et seulement si cela fonctionne
     * */
    public boolean addTransaction(Transaction t){
        if(blockchainRepository.db.getBlockchainList()==null)blockchainRepository.getData();
        if(blockchainRepository.db.getBlockchainList().size()==0)createBlockchain(4);
        Blockchain blockchain = blockchainRepository.db.getBlockchainList().get(0);
        if(blockchain==null)return false;
        Bloc b = Bloc.builder().data(t).hash(null).id(blocService.maxId()+1).index(blockchain.getBlocs().size()).nonce(0)
                .previousHash(blockchain.getBlocs().get(blockchain.getBlocs().size()-1).getHash())
                .timestamp(System.currentTimeMillis()).build();
        if(addBloc(b))return true;
        return false;
    }

    /**Méthode pour récupérer tous les blocs de la blockchain
     * @return la liste de blocs, null si erreur
     * */
    public List<Bloc> getAll(){
        if(blockchainRepository.db.getBlockchainList()==null)blockchainRepository.getData();
        if(blockchainRepository.getById(0)==null)createBlockchain(4);
        Blockchain blockchain = blockchainRepository.getById(0);
        System.out.println("blockchain getted :"+blockchain);
        if(blockchain==null)return null;
        return blockchain.getBlocs();
    }
}
