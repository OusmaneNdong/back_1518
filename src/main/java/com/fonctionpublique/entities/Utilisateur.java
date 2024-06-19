package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String typePieces;
    private String nin;
    private String passPort;
    private String statut;
    private String signature;
    @OneToOne
    private Demandeur demandeur;
    @OneToMany(mappedBy = "utilisateur")
    private  List<Demande> demande;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public String getFullName(){
        return prenom + " " + nom;
    }

//    public Utilisateur(String prenom, String nom, String email, String password, String nin, String statut,Profile profile,) {
//        this.prenom = prenom;
//        this.nom = nom;
//        this.email = email;
//        this.password = password;
//        this.nin = nin;
//        this.statut = statut;
//        this.profile = profile;
//    }

//    public Utilisateur(int id, String prenom, String nom, String email, String password, String typePieces, String nin, String passPort, String statut, String signature, Demandeur demandeur, List<Demande> demande, Profile profile) {
//        this.id = id;
//        this.prenom = prenom;
//        this.nom = nom;
//        this.email = email;
//        this.password = password;
//        this.typePieces = typePieces;
//        this.nin = nin;
//        this.passPort = passPort;
//        this.statut = statut;
//        this.signature = signature;
//        this.demandeur = demandeur;
//        this.demande = demande;
//        this.profile = profile;
//    }
}
