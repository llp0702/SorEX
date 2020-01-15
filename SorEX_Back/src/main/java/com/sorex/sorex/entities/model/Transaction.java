package com.sorex.sorex.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**Classe pour modéliser une transaction. Nous supposons que toute transaction créée
 *est réalisable (c'est à dire que le porte-monnaie source dispose d'assez de Sordex*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    /**Identifiant unique / Clé primaire de la transaction*/
    private int idTransaction;

    /**Description de la transaction*/
    private String description;

    @JsonIgnore
    /**Porte-monnaie de destination de la transaction*/
    private Wallet dest;
    @JsonIgnore
    /**Porte-monnaie source de la transaction*/
    private Wallet src;

    /**Montant de la transaction*/
    private int montant;

    /**Moment (date et instant) de la transaction*/
    private LocalDateTime date;
    @JsonIgnore
    /**Object concerné par la transaction*/
    private Item item;

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", description='" + description + '\'' +
                ", dest=" + ((dest==null)?"null":dest.getIdWallet()) +
                ", src=" + ((src==null)?"null":src.getIdWallet()) +
                ", montant=" + montant +
                ", date=" + date +
                ", item=" + item +
                '}';
    }
}
