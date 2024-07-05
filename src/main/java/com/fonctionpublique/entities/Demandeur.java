package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Demandeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String telephone;
    private String datedenaissance;
    private String lieudenaissance;
    private String adresse;
    private String sexe;
    private String fonction;
    @Column(unique = true)
    private String nin;
    private String scannernin;
    private String statut;
    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    @OneToMany
    private List<Demande> demande;

    public boolean isCompleted() {
        if (telephone == null || datedenaissance == null || lieudenaissance == null
                || adresse == null || sexe == null || fonction == null || nin == null
                || scannernin == null || statut == null) {
            return false;
        }
        return true;

    }


}
