package com.sorex.sorex.entities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**Classe permettant de modéliser la notion de types d'objets. Concrétement, cela
 * représenterait au niveau métier la notion de Livre, de Vélo ou autres*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Type implements Serializable {
    /**Identifiant unique / clé primaire du type d'objet*/
    private int idType;

    /**Nom du type*/
    private String nom;

    /**Valeur du bonus associé à ce type. Concrétement, quand un utilisateur
     * propose un Objet, on le récompense par un bonus en Sarex dont la valeur
     * est spécifiée par cet attribut*/
    private int bonus;


}
