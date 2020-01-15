package com.sorex.sorex.controllers;

import com.sorex.sorex.entities.blockchain.Bloc;
import com.sorex.sorex.services.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**Controller associé à la blockchain, offre des services web relatifs à la blockchain*/
@RestController
public class BlockchainController {
    /**Dépendance aux services de la blockchain, injectée automatiquement*/
    @Autowired private BlockchainService blockchainService;

    /**Méthode pour récupérer les blocs de la blockchain*/
    @GetMapping(value="/blockchain/bloc/get/all")
    public List<Bloc> getAllBlocs(){
        return blockchainService.getAll();
    }

}
