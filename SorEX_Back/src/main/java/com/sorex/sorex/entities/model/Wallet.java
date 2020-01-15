package com.sorex.sorex.entities.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**Classe permettant de modéliser la notion de porte monnaie. Chaque client dispose
 * d'un porte monnaie*/
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Wallet implements Serializable  {

    /**Mot de passe du wallet*/
    private String password;

    /**Identifiant unique / Clé primaire du porte monnaie*/
    private int idWallet;


    /**Liste des transactions du porte-monnaie*/
    private List<Transaction> transactions;

    /**Valeur du solde du porte-monnaie*/
    private int balance;

    @Override
    public String toString() {
        return "Wallet{" +
                "password='" + password + '\'' +
                ", idWallet=" + idWallet +
                ", transactions=" + transactions +
                ", balance=" + balance +
                '}';
    }
}
