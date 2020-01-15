package com.sorex.sorex.services;

import com.sorex.sorex.entities.model.Item;
import com.sorex.sorex.entities.model.Transaction;
import com.sorex.sorex.entities.model.Wallet;
import com.sorex.sorex.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**Classe se chargant d'assurer les services des transactions. Elle manipule l'entrepot de données
 * des transactions*/
@Service
public class TransactionService {

     /**Dépendance à l'entrepot de données injectée automatiquement. Permet d'accéder aux données*/
     @Autowired
     private TransactionRepository transactionRepository;

     /**Dépendance aux services des portes monnaie, injectée automatiquement*/
     @Autowired
     private WalletService walletService;

     /**Dépendance au service des items, injectée automatiquement*/
     @Autowired private ItemService itemService;

     /**Méthode pour effectuer un virement d'un porte monnaie vers un autre.
      * @param idSrc identifiant du porte monnaie source
      * @param idDest identifiant du porte monnaie destination
      * @param value montant du virement en question
      * @return La tranasction effectuée, null en cas d'erreur*/
     public Transaction transferSorEx(int idSrc, int idDest, int value){
         Wallet src=null,dest=null;
         if((src=walletService.getById(idSrc))==null)return null;
         if((dest=walletService.getById(idDest))==null)return null;
         if(src.getBalance()<value)return null;
         if(src.getTransactions()==null)src.setTransactions(new ArrayList<Transaction>());
         if(dest.getTransactions()==null)src.setTransactions(new ArrayList<Transaction>());
         int idTransaction = transactionRepository.getIdMax() +1;
         if(idTransaction==0)return null;
         Transaction transaction = Transaction.builder().idTransaction(idTransaction)
                 .date(LocalDateTime.now(ZoneId.systemDefault()))
                 .description("Transaction du porte monnaie numero "+idSrc+" au porte monnaie numero "+
                         idDest+" d'un montant "+value)
                 .src(src)
                 .dest(dest)
                 .item(null)
                 .montant(value)
                 .build();
         src.setBalance(src.getBalance()-value);
         dest.setBalance(dest.getBalance()+value);
         src.getTransactions().add(transaction);
         dest.getTransactions().add(transaction);
         if(!transactionRepository.addNewTransaction(transaction))return null;
         return transaction;
     }
     /**Méthode pour transférer du SorEx dans le cadre d'un prêt
      * @param idSrc identifiant du porte monnaie source
      * @param idDest identifiant du porte monnaie destinataire
      * @param value montant de la transaction
      * @param idItem identifiant de l'item concerné
      * @return la transaction résultante, et null en cas d'erreur
      * */
     public Transaction transferSorEx(int idSrc, int idDest, int value, int idItem){
         Wallet src=null,dest=null;
         Item item;
         if((src=walletService.getById(idSrc))==null)return null;
         if((dest=walletService.getById(idDest))==null)return null;
         if(src.getBalance()<value)return null;
         if((item=itemService.getById(idItem))==null)return null;
         if(src.getTransactions()==null)src.setTransactions(new ArrayList<Transaction>());
         if(dest.getTransactions()==null)src.setTransactions(new ArrayList<Transaction>());
         int idTransaction = transactionRepository.getIdMax() +1;
         if(idTransaction==0)return null;
         Transaction transaction = Transaction.builder().idTransaction(idTransaction)
                 .date(LocalDateTime.now(ZoneId.systemDefault()))
                 .description("Transaction du porte monnaie numero "+idSrc+" au porte monnaie numero "+
                         idDest+" d'un montant "+value+ "Pour prêter l'item numéro "+idItem+" du "+
                         LocalDate.now(ZoneId.systemDefault())+" au "+item.getDateDeRetour())
                 .src(src)
                 .dest(dest)
                 .item(item)
                 .montant(value)
                 .build();
         src.setBalance(src.getBalance()-value);
         dest.setBalance(dest.getBalance()+value);
         src.getTransactions().add(transaction);
         dest.getTransactions().add(transaction);
         if(!transactionRepository.addNewTransaction(transaction))return null;
         return transaction;
     }
     /**Méthode pour transferer du SorEx sans source (dans le cas de l'attribution des bonus)
      * @param idDest le wallet à créditer
      * @param value montant de la transaction
      * @param idItem item concerné par la transaction
      * @return transaction resultante, null en cas d'erreur
      * */
     public Transaction transferSorExBonus(int idDest, int value, int idItem){
         Wallet dest=null;
         Item item;
         if((dest=walletService.getById(idDest))==null)return null;
         if((item=itemService.getById(idItem))==null)return null;
         int idTransaction = transactionRepository.getIdMax() +1;
         if(idTransaction==0)return null;
         Transaction transaction = Transaction.builder().idTransaction(idTransaction)
                 .date(LocalDateTime.now(ZoneId.systemDefault()))
                 .description("Transaction liée à un bonus destiné au porte monnaie numero "+
                         idDest+" d'un montant "+value+ "Pour l'item numéro "+idItem)
                 .src(null)
                 .dest(dest)
                 .item(item)
                 .montant(value)
                 .build();
         dest.setBalance(dest.getBalance()+value);
         dest.getTransactions().add(transaction);
         if(!transactionRepository.addNewTransaction(transaction))return null;
         return transaction;
     }

}
