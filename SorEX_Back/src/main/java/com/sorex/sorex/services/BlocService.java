package com.sorex.sorex.services;

import com.sorex.sorex.entities.blockchain.Bloc;
import com.sorex.sorex.entities.blockchain.Utils;
import com.sorex.sorex.repository.BlocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**Service associé aux blocs d'une blockchain*/
@Service
public class BlocService {
    /**Dépendance à l'entrepôt de données liées aux Blocs*/
    @Autowired private BlocRepository blocRepository;

    /**Méthode qui retourne le hash d'un bloc par son id
     * @param idBloc identifiant du bloc
     * @return la hash générée, null en cas d'erreur
     * */
    public String calculateHash(int idBloc){
        Bloc bloc = blocRepository.getById(idBloc);
        if(bloc==null)return null;
        return calculateHash(bloc);
    }

    /**Méthode pour supprimer tous nos blocs
     * @return true si tout s'est bien passé, false sinon
     * */
    public boolean clearAllBlocs(){
        return blocRepository.clearBlocs();
    }


    /**Méthode qui retourne le hash d'un bloc par son id
     * @param bloc bloc concerné
     * @return la hash générée, null en cas d'erreur
     * */
    public String calculateHash(Bloc bloc){
        if (bloc != null) {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
            String txt = bloc.str();
            final byte bytes[] = digest.digest(txt.getBytes());
            final StringBuilder builder = new StringBuilder();
            for (final byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        }
        return null;
    }

    /**
     * Method that allows us to mine block by resolving the
     * proof of work for current block with a set difficulty.
     *
     * Mining will continue as long as calculated hash
     * is different from expected hash.
     *
     * When Proof of work is resolved, the block is mined.
     *
     * @param idBloc bloc's id
     * @param difficulty number of 0s at the beginning of
     *                   the hash for the current block
     * @return true if and only if the operation succeeded
     */
    public boolean mineBloc(int idBloc, int difficulty) {
        Bloc b;
        if((b=blocRepository.getById(idBloc))==null)return false;
        b.setNonce(0);

        while(b.getHash()==null || !b.getHash().substring(0, difficulty).equals(Utils.zeros(difficulty))) {
            b.setNonce(b.getNonce()+1);
            b.setHash(calculateHash(b));
        }
        return true;
    }
    /**@return l'identifiant maximal des blocs*/
    public int maxId(){
        return blocRepository.maxId();
    }

    public void store(Bloc b){
        blocRepository.store(b);
    }

}
