package com.sorex.sorex.repository;

import com.sorex.sorex.entities.blockchain.Bloc;
import com.sorex.sorex.entities.blockchain.Blockchain;
import com.sorex.sorex.entities.model.*;
import com.sorex.sorex.entities.model.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**Source primaire de données, fait office de base de données*/
@Repository
@Getter
@Setter
public class DB implements Serializable {
    /**Chemin vers le fichier contenant toutes les données*/
    private static String pathFile = "src/main/resources/data";

    /**Liste des items de l'application*/
    private List<Item> itemList;
    /**Liste des blocks de l'application*/
    private List<Bloc> blocList;
    /**Liste des blockchains de l'application*/
    private List<Blockchain> blockchainList;
    /**Liste des entrepots de l'application*/
    private List<Warehouse> warehouseList;
    /**Liste des transactions de l'application*/
    private List<Transaction> transactionList;
    /**Liste des types de l'application*/
    private List<Type> typeList=new ArrayList<Type>(
            Arrays.asList(
                    Type.builder().bonus(2).idType(1).nom("Bike").build(),
                    Type.builder().bonus(1).idType(2).nom("Book").build(),
                    Type.builder().bonus(3).idType(3).nom("Computer").build()
            )

    );
    /**Liste des wallets de l'application*/
    private List<Wallet> walletList;

    /**Méthode permettant de sauvegareder les données de l'application
     * @return true si et seulement si la fonction se termine avec succès et false sinon
     * */
    synchronized public boolean save(){
        try{
            ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(pathFile));
            writer.writeObject(this);
            writer.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    /**Méthode permettant de charger toutes les données de l'application
     * @return true si et seulement si l'operation s'effectue avec succès, sinon false
     * */
    synchronized public boolean load(){
        try{
            ObjectInputStream reader = new ObjectInputStream(
                    new FileInputStream(pathFile)
            );
            DB r = (DB)reader.readObject();
            reader.close();
            itemList = r.itemList;
            blocList = r.blocList;
            blockchainList = r.blockchainList;
            warehouseList = r.warehouseList;
            transactionList = r.transactionList;
            //typeList = r.typeList;
            walletList = r.walletList;
            if(itemList==null)itemList=new ArrayList<Item>();
            if(blocList ==null) blocList = new ArrayList<Bloc>();
            if(blockchainList==null)blockchainList = new ArrayList<Blockchain>();
            if(warehouseList ==null) warehouseList = new ArrayList<Warehouse>();
            if(transactionList==null)transactionList = new ArrayList<Transaction>();
            //if(typeList==null)typeList= new ArrayList<Type>();
            if(walletList==null)walletList = new ArrayList<Wallet>();
            return true;
        }catch(EOFException e){
            itemList=new ArrayList<Item>();
            blocList = new ArrayList<Bloc>();
            blockchainList = new ArrayList<Blockchain>();
            warehouseList = new ArrayList<Warehouse>();
            transactionList = new ArrayList<Transaction>();
            //typeList= new ArrayList<Type>();
            walletList = new ArrayList<Wallet>();
            return true;
        }catch(FileNotFoundException e){
            try{
                new File(pathFile).createNewFile();
                itemList=new ArrayList<Item>();
                blocList = new ArrayList<Bloc>();
                blockchainList = new ArrayList<Blockchain>();
                warehouseList = new ArrayList<Warehouse>();
                transactionList = new ArrayList<Transaction>();
                //typeList= new ArrayList<Type>();
                walletList = new ArrayList<Wallet>();
                return true;
            }catch(IOException ex){
                e.printStackTrace();
                return false;
            }
        }catch(Exception e){
            System.out.println(System.getProperty("user.dir"));
            e.printStackTrace();
            return false;
        }
    }
}
