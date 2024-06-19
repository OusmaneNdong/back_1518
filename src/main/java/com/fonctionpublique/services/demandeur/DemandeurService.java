package com.fonctionpublique.services.demandeur;

import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.Demandeur;

import java.util.List;
import java.util.Optional;

public interface DemandeurService {
    DemandeurDTO getByNin(String nin);

    Optional<Demandeur> getByStatut(String statut);

    DemandeurDTO getById(int id);

    public List<DemandeurDTO> findAll();
    DemandeurDTO update(DemandeurDTO demandeurDTO);
}
