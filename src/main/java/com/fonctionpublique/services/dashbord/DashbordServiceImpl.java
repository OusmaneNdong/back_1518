package com.fonctionpublique.services.dashbord;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DashbordServiceImpl  implements DashbordService {

    private final DemandeurServiceImpl demandeurServiceImpl;
    private final DemandeRepository demandeRepository;
    private final DemandeServiceImpl demandeService;

    @Override
    public ResponseEntity<Integer> getCount() {
        List<DemandeDTO> demades = demandeService.findAll();
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getApprouved() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }
    @Override
    public ResponseEntity<Integer> getRejected() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }

    @GetMapping("/getCours")
    public ResponseEntity<Integer> getCours() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeCount() {
        List<DemandeDTO> demades = demandeService.findAll();
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeApprouved() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeRejected() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeCours() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }

}


