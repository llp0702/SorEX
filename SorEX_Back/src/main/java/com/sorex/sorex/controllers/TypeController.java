package com.sorex.sorex.controllers;

import com.sorex.sorex.entities.model.Type;
import com.sorex.sorex.services.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**Controller relatif aux types d'items, il expose des services web en REST*/
@RestController
public class TypeController {
    /**Dépendance au Service relatif aux types, injectée automatiquement*/
    @Autowired
    TypeService typeService;

    /**Méthode pour récupérer tous les types d'items
     * @return la liste de tous les types, null en cas d'erreur
     * */
    @GetMapping(value="/type/get/all")
    public List<Type> getAllTypes(){
        return typeService.getAll();
    }
}
