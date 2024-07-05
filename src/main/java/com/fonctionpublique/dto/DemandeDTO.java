package com.fonctionpublique.dto;

import com.fonctionpublique.entities.Certification;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeDTO {

    private int id;
    private String urlattestation;
    private String statut;
    private String numerodemande;
    private LocalDateTime datedemande;
    private LocalDateTime datetraitement;
    private String attestaionName;
    private boolean validite;
    private String objetdemande;
    private String descriptiondemande;
    private CertificationDTO certificationDTO;
    //private UtilisateurDTO utilisateurDTO;
    private DemandeurDTO demandeurDTO;
}
