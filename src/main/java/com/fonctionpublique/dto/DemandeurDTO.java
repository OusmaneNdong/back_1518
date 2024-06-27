package com.fonctionpublique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeurDTO {
    private int id;
    private String telephone;
    private String datedenaissance;
    private String lieudenaissance;
    private String adresse;
    private String sexe;
    private String fonction;
    private String nin;
    private String scannernin;
    private String statut;
    private UtilisateurDTO utilisateurDTO;
    private String email;
    private String prenom;
    private String nom;
    private String fullName;
    private boolean isCompleted;
    private int userId;
    private List<DemandeDTO> demandeDTO;
}
