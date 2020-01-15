package com.sorex.sorex.entities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**Classe modélisant les objets sujets à location, il peut s'agir d'un livre, d'un vélo
 * ou n'importe quoi que pourrait prêter un utilisateur simple*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
    /**Identifiant unique / Clé primaire de l'objet*/
    private int idItem;

    /**Date du retour de cet objet. Si null, cela veut dire que l'objet est
     * disponible, sinon : si la date de retour est dépassée, cela veut
     * dire que le prêt n'a pas été respecté, auquel cas des sanctions sont appliquées*/
    private LocalDate dateDeRetour;

    /**Prix unitaire de l'objet par jour*/
    private int prixParJour;

    /**Titre de l'objet*/
    private String title;

    /**Description de l'objet*/
    private String description;

    /**Type de l'objet*/
    private Type type;

    /**Propriétaire de l'objet, ne dois pas valoir null*/
    private Wallet proprietaire;

    /**Emprunteur de l'objet, vaut null si l'objet est disponible*/
    private Wallet emprunteur;

    @Override
    public String toString() {
        return "Item{" +
                "idItem=" + idItem +
                ", dateDeRetour=" + dateDeRetour +
                ", prixParJour=" + prixParJour +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", proprietaire=" + ((proprietaire==null)?"null":proprietaire.getIdWallet()) +
                ", emprunteur=" + ((emprunteur==null)?"null":emprunteur.getIdWallet()) +
                '}';
    }
}
