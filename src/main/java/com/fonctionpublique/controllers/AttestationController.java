package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.*;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.mailing.StatutMail;
import com.fonctionpublique.repository.*;
import com.fonctionpublique.services.attestation.AttestationServiceImpl;
import com.fonctionpublique.services.compteur.CompteurServiceImpl;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/attestation")
@RequiredArgsConstructor
public class AttestationController {

    private final AttestationServiceImpl attestationServiceImpl;
    private final UtilisateurRepository utilisateurRepository;
    private final DemandeurRepository demandeurRepository;
    private final DemandeRepository demandeRepository;
    private final StructureRepository structureRepository;
    private final CompteurServiceImpl compteurServiceImpl;
    private final CompteurRepository compteurRepository;
    private final StatutMail statutMail;


   @GetMapping("/pdf_genere/{idUser}/{idDemandeur}/{idDemande}/{idStructure}")
    public String getAttestion(@PathVariable("idUser") int idUser , @PathVariable("idDemandeur") int idDemandeur, @PathVariable("idDemande") int idDemande,@PathVariable("idStructure") int idStructure) throws IOException, WriterException {
       Utilisateur u = utilisateurRepository.findById(idUser).orElse(null);
       Demandeur d = demandeurRepository.findById (idDemandeur).orElse(null);
       Structure s = structureRepository.findById(idStructure).orElse(null);
       Demande demande = demandeRepository.findById(idDemande).orElse(null);

       if (d!=null && u!=null && s!=null && demande!=null){
           attestationServiceImpl.getAttestationPdf(u,d,demande,s);

           Demande demand = demandeRepository.findById(idDemande).orElse(null);
           demand.setDatetraitement(LocalDateTime.now());
           demandeRepository.save(demand);

           d.setStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
           demandeurRepository.save(d);
           Optional<Compteur> optionalCompteur = compteurRepository.findById(1);
           if(optionalCompteur.isPresent()){
               Compteur compteur =  optionalCompteur.get();
               compteur.setCurrentCount(compteurServiceImpl.incrementCounter());
               compteurRepository.save(compteur);
           }

           statutMail.sentMailApprouved(u,d,demande,s);


           return "generer";
       }else{
           d.setStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
           demandeurRepository.save(d);
           statutMail.sentMailRejeted(d);
       }

       return "echec";
   }

   public Integer count(int id){
       Optional<Demandeur> optionalDemandeur = demandeurRepository.findById (id);
       int a = 0;
       if(optionalDemandeur.isPresent()){
           if(optionalDemandeur.get().getStatut().equalsIgnoreCase("approuvée")){
               int d = (int) optionalDemandeur.stream().count();
               return d;
           }
       }
       return null;
   }



}
