package com.sorex.sorex.services;

import com.sorex.sorex.entities.model.Type;
import com.sorex.sorex.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**Classe se chargant d'assurer les services des types d'items. Elle manipule l'entrepot de données
 * des items*/
@Service
public class TypeService {
    /**Dépendance à la source des données brutes, injectée automatiquement*/
    @Autowired private TypeRepository typeRepository;

    /**Méthode pour récupérer un type par son identifiant
     * @param id identifiant du type
     * @return le type concerné, null en cas d'erreur
     */
    public Type getById(int id){
        return typeRepository.getById(id);
    }

    /**Méthode pour récupérer tous les types
     * @return Liste des types, null en cas d'erreur*/
    public List<Type> getAll(){
        return typeRepository.getAll();
    }

    /**Méthode pour calculer un bonus selon son coefficient
     * @param from coefficient associé au bonus
     * @return la valeur du bonus selon la fonction de calcul du bonus*/
    public int computeBonus(int from){
        return (int)from/(3+(int)Math.log((double)from));
    }
}
