package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/demande")
@RequiredArgsConstructor
@CrossOrigin
public class DemandeController {

    private final DemandeServiceImpl demandeServiceImpl;

    @PostMapping("/demandez/{id}")
    public Integer demander(@RequestBody Demande demande, @PathVariable("id") int id) throws IOException, WriterException {
        return demandeServiceImpl.creerDemande(demande,id);
    }
    @GetMapping("/demandeDetails/{id}")
    public ResponseEntity<DemandeDTO> getById(@PathVariable int id){
        return  ResponseEntity.ok(demandeServiceImpl.getById(id));

    }
    @GetMapping("/getDemande")
    public List<DemandeDTO> findAll(){

        return demandeServiceImpl.findAll();
    }
    @GetMapping("/getStatut/{statut}")
      public List<DemandeDTO> findAllDemande(@PathVariable("statut") String statut) {
        return demandeServiceImpl.findByStatut(statut);
    }


}
