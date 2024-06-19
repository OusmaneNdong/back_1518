package com.fonctionpublique.dto;

import com.fonctionpublique.entities.Demande;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationDTO {

    private long id;
    private String type;
    private String code;
    //private DemandeDTO demandeDTO;
}
