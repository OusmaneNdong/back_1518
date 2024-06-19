package com.fonctionpublique.services.demandeur;

import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class DemandeurServiceImpl implements  DemandeurService {

    private final DemandeurRepository demandeurRepository;
    private final UtilisateurRepository utilisateurRepository;
    //private final DemandeServiceImpl demandeServiceImpl;

    public String creerDemandeur(DemandeurDTO demandeurDTO) {

        Optional<Utilisateur> optionalUtilisateurs = utilisateurRepository.findByNin(demandeurDTO.getNin());

        if (optionalUtilisateurs.isPresent()) {

            Demandeur demandeur = new Demandeur();
            demandeur.setTelephone(demandeurDTO.getTelephone());
            demandeur.setDatedenaissance(demandeurDTO.getDatedenaissance());
            demandeur.setLieudenaissance(demandeurDTO.getLieudenaissance());
            demandeur.setAdresse(demandeurDTO.getAdresse());
            demandeur.setSexe(demandeurDTO.getSexe());
            demandeur.setFonction(demandeurDTO.getFonction());
            demandeur.setNin(demandeurDTO.getNin());
            demandeur.setScannernin(demandeurDTO.getScannernin());
            demandeur.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
            demandeur.setUtilisateur(optionalUtilisateurs.get());
            demandeurRepository.save(demandeur);

            optionalUtilisateurs.get().setDemandeur(demandeur);
            utilisateurRepository.save(optionalUtilisateurs.get());
            return "true";

        }
        return "false";
    }

    @Override
    public DemandeurDTO getByNin(String nin) {
        return convertToDTO(Objects.requireNonNull(demandeurRepository.findByNin(nin).orElse(null))) ;
    }

    @Override
    public Optional<Demandeur> getByStatut(String statut) {
            return demandeurRepository.findByStatut(statut);

    }

    @Override
    public DemandeurDTO getById(int id) {
//        return convertToDTO(Objects.requireNonNull(demandeurRepository.findById(id).orElse(null))) ;
        Optional<Demandeur> demandeur = Optional.ofNullable(demandeurRepository.findById(id).orElseThrow(null));
        if(demandeur.isPresent()){
            return convertToDTO(demandeur.get());
        }
        return null;
    }
    public DemandeurDTO convertToDTO(Demandeur demandeur){

        return DemandeurDTO.builder()
                .id(demandeur.getId())
                .adresse(demandeur.getAdresse())
                .nom(demandeur.getUtilisateur().getNom())
                .prenom(demandeur.getUtilisateur().getPrenom())
                .email(demandeur.getUtilisateur().getEmail())
                .sexe(demandeur.getSexe())
                .datedenaissance(demandeur.getDatedenaissance())
                .lieudenaissance(demandeur.getLieudenaissance())
                .fonction(demandeur.getFonction())
                .nin(demandeur.getNin())
                .telephone(demandeur.getTelephone())
                .scannernin(demandeur.getScannernin())
                .userId(demandeur.getUtilisateur().getId())
                .statut(demandeur.getStatut())
                .fullName(demandeur.getUtilisateur().getFullName())
                .build();

    }

    @Override
    public List<DemandeurDTO> findAll() {
        return demandeurRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    @Override
    public DemandeurDTO update(DemandeurDTO demandeurDTO) {
        Demandeur demandeur = new Demandeur();
        BeanUtils.copyProperties(demandeurDTO,demandeur);
        return  convertToDTO(demandeurRepository.save(demandeur));
    }
}
