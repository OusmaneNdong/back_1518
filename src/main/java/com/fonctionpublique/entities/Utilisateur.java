package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String prenom;
    private String nom;
    @Column(unique = true)
    private String email;
    private String password;
    private String typePieces;
    @Column(unique = true)
    private String nin;
    private String passPort;
    private boolean statut;
    private String signature;
    @OneToOne
    private Demandeur demandeur;
    @OneToMany(mappedBy = "utilisateur")
    private List<Demande> demande;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    public String getFullName() {
        return prenom + " " + nom;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(profile.getCode()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return statut;
    }

}