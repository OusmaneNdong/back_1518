package com.fonctionpublique.services.demande;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.List;

public interface DemandeService {
    List<DemandeDTO> findAll();

    DemandeDTO getById(int id);

    //    boolean creerDemande(DemandeDTO demandeDTO);
    Integer creerDemande(Demande demande, int id) throws IOException, WriterException;
    List<DemandeDTO> findAllDemande() throws IOException, WriterException;

    List<DemandeDTO> findByStatut(String status);

    List<DemandeDTO> findByDemandeurId(int id);


}
