package com.fonctionpublique.services.demande;

import com.fonctionpublique.dto.DemandeDTO;

import java.util.List;

public interface DemandeService {
    List<DemandeDTO> findAll();

    DemandeDTO getById(int id);

    Integer creerDemande(int id);

    List<DemandeDTO> findByStatut(String status);

    List<DemandeDTO> findByDemandeurId(int id);


    String findAttestation(int id);
}
