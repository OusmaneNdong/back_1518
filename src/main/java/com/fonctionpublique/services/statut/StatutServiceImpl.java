package com.fonctionpublique.services.statut;

import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.mailing.StatutMail;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.services.attestation.AttestationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.FileNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class StatutServiceImpl implements StatutService {

    private final UtilisateurRepository utilisateurRepository;
    private final DemandeurRepository demandeurRepository;
    private final DemandeRepository demandeRepository;
    private final StatutMail statutMail;

    @Override
    public String approuvedStatut(Utilisateur u, Demandeur d, Demande dm, Structure s) throws FileNotFoundException {
        Optional<Utilisateur> optional = utilisateurRepository.findByEmail(u.getEmail());
                if(optional.isPresent()){
                    statutMail.sentMailApprouved(u,d, dm, s);
                }else {
                    System.out.println("email introuvable");
                }
                return "approuved mail sent successfully !";
    }
    public String rejetedStatut(Integer id)  {
        //Optional<Utilisateur> optional = utilisateurRepository.findByEmail(email);
        Optional<Demande> demande = demandeRepository.findById(id);

        if(demande.isPresent()){
            Demandeur demandeur = demande.get().getDemandeur();
            statutMail.sentMailRejeted(demandeur);

            //Optional<Demandeur> optionalDemandeur  = demandeurRepository.findById(optional.get().getDemandeur().getId());
            //if(optionalDemandeur.isPresent()){
               // Demandeur demandeur = demande.get().getDemandeur();
                demande.get().setStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
                demandeRepository.save(demande.get());
            //}
        }else {
            System.out.println("email introuvable");
        }
        return "rejected mail sent successfully !";
    }

}
