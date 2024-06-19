package com.fonctionpublique.controllers;

import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.mailing.StatutMail;
import com.fonctionpublique.services.statut.StatutServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@CrossOrigin
public class SendMailController {

    private final StatutMail statutMail;
    private final StatutServiceImpl statutServiceImpl;

    @PostMapping("/mail_approuved")
    public String approuved(@RequestBody Utilisateur u, Demandeur d, Demande dm, Structure s) throws FileNotFoundException {
        return statutServiceImpl.approuvedStatut( u,  d,  dm,  s);
    }
    @GetMapping("/mail_rejete/{id}")
    public String rejeted(@PathVariable("id") Integer id){

        return statutServiceImpl.rejetedStatut(id);
    }


}
