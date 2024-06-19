package com.fonctionpublique.controllers;

import com.fonctionpublique.services.compteur.CompteurServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compteur/")
@CrossOrigin
@RequiredArgsConstructor
public class CompteurController {

    private final CompteurServiceImpl compteurServiceImpl;

//    @PostConstruct
//    public void compteurTotalApprouved(){
//        compteurServiceImpl.compteurTotlaApprouved();
//    }

}
