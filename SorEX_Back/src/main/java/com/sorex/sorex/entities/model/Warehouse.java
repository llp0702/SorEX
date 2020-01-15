package com.sorex.sorex.entities.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**Classe pour modéliser un entrepot d'objets, il pourrait représenter la Bibliothèque
 * de l'université d'un point de vue métier.*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse implements Serializable {
    /**Identifiant unique / clé primaire de l'entrepôt*/
    private int idWarehouse;

    /**Adresse de l'entrepot*/
    private String adresse;

    /**Objets proposés par l'entrepôt*/
    private List<Item> items;
}
