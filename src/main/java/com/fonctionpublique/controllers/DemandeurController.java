package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.EmailDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/demandeur")
@RequiredArgsConstructor
public class DemandeurController {

    private final DemandeurServiceImpl demandeurServiceImpl;
    @PostMapping("/demander")
    public ResponseEntity<String> incription(@RequestBody DemandeurDTO demandeurDTO){
            return ResponseEntity.ok(demandeurServiceImpl.creerDemandeur(demandeurDTO));
    }
//    @PutMapping("/demander")
//    public ResponseEntity<DemandeurDTO> update(@RequestBody DemandeurDTO demandeurDTO){
//
//        return ResponseEntity.ok(demandeurServiceImpl.update(demandeurDTO));
//    }
    @GetMapping("/getDemandeur")
    public List<DemandeurDTO> findAll(){
        return demandeurServiceImpl.findAll();
    }
    @GetMapping("/envoie")
    public String sendEmail(){
        return "ok";
    }
    @Autowired
    private JavaMailSender javaMailSender;
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailDTO emailDTO){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailDTO.getTo());
        simpleMailMessage.setSubject(emailDTO.getSubject());
        simpleMailMessage.setText(emailDTO.getText());

        javaMailSender.send(simpleMailMessage);

        return "Email sent succesfully";
    }
    @GetMapping("/details/{nin}")
    public ResponseEntity<DemandeurDTO> getByNin(@PathVariable String nin){
        return  ResponseEntity.ok(demandeurServiceImpl.getByNin(nin));
    }
    @GetMapping("/getStatut/{statut}")
    public ResponseEntity<DemandeurDTO> getByStatus(@PathVariable String statut){
        DemandeurDTO demandeurDTO = new DemandeurDTO();
        Optional<Demandeur> optionalUtilisateurOutilisateur = demandeurServiceImpl.getByStatut(statut);
        return ResponseEntity.ok(demandeurServiceImpl.convertToDTO(optionalUtilisateurOutilisateur.get()));
    }
//    @GetMapping("/getStatut/{statut}")
//    public ResponseEntity<DemandeurDTO> getByStatut(@PathVariable String statut){
//        DemandeurDTO demandeurDTO = new DemandeurDTO();
//        DemandeurDTO optionalDemandeur = demandeurServiceImpl.getByStatut(statut);
//        if(optionalDemandeur.isPresent()){
//            Demandeur demandeur = optionalDemandeur.get();
//            BeanUtils.copyProperties(demandeur,demandeurDTO);
//        }
//        return ResponseEntity.ok(demandeurDTO);
//    }
//    public ResponseEntity<DemandeurDTO> getStatut(@RequestParam String statut){
//        return ResponseEntity.ok(demandeurServiceImpl.getByStatut(statut));
//    }
    @GetMapping("/demandeurDetails/{id}")
    public ResponseEntity<DemandeurDTO> getById(@PathVariable int id){
        return  ResponseEntity.ok(demandeurServiceImpl.getById(id));

    }
}
