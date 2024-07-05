package com.fonctionpublique.services.demandeur;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;

import java.util.List;
import java.util.Optional;

public interface DemandeurService {
    DemandeurDTO getByNin(String nin);
    DemandeurDTO getById(int id);
    public List<DemandeurDTO> findAll();


}
