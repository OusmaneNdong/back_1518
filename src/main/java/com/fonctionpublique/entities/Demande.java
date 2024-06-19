package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String urlattestation;
    private String statut;
    private String numerodemande;
    private LocalDateTime datedemande;
    private LocalDateTime datetraitement;
    private boolean validite;
    private String objetdemande;
    private String descriptiondemande;
    private String attestationName;
    @Transient
    private String nin;
    @OneToOne
    private Certification certification;
    @ManyToOne
    @JoinColumn(name = "id_demandeur")
    private Demandeur demandeur;
    @ManyToOne
    @JoinColumn( name = "id_utilisateur")
    private Utilisateur utilisateur;

    public boolean isValide(){
        LocalDate localDateTime = datetraitement.plusMonths(9).toLocalDate();
        Calendar c = Calendar.getInstance();
        //LocalDateTime now = c.getTime().getTime();
        if (localDateTime.isAfter(LocalDate.now())){
            return true;
        }
        return false;
    }

}
