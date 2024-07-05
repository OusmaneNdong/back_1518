package com.fonctionpublique.services.demande;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.enumpackage.TypeDemande;
import com.fonctionpublique.repository.CertificationRepository;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.services.certification.CertificationServiceImpl;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import com.fonctionpublique.validators.ObjectValidator;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class DemandeServiceImpl implements DemandeService {

    private final DemandeurRepository demandeurRepository;
    private final DemandeRepository demandeRepository;
    private final CertificationServiceImpl certificationServiceImpl;
    private final CertificationRepository certificationRepository;
    private final DemandeurServiceImpl demandeurService;
    private final ObjectValidator<DemandeDTO> validator;

    /**
     * List all demandes
     *
     * @return
     */
    @Override
    public List<DemandeDTO> findAll() {
        return demandeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * return a demande by id
     *
     * @param id
     * @return
     */
    @Override
    public DemandeDTO getById(int id) {
        Optional<Demande> demande = demandeRepository.findById(id);
        if (demande.isPresent()) {
            return convertToDTO(demande.get());
        }
        return null;
    }

    /**
     * create demande
     *
     * @param id
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @Override
    public Integer creerDemande(int id) throws IOException, WriterException {
        Optional<Demandeur> demandeur = demandeurRepository.findById(id);//.orElseThrow(() -> new EntityNotFoundException("Pas de demandeur cet ID " + id));
        if (!demandeur.isPresent()) {
            throw new EntityNotFoundException("NOT_FOUND");
        }
        Demande demande = new Demande();
        demande.setDemandeur(demandeur.get());
        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        demande.setDatedemande(LocalDateTime.now());
        demande.setDatetraitement(LocalDateTime.now());
        demande.setValidite(true);
        demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
        demande.setDescriptiondemande("Description de la demande");

        return demandeRepository.save(demande).getId();
    }

    /**
     * Convert entity demande to dto demande
     *
     * @param demande
     * @return
     */

    public DemandeDTO convertToDTO(Demande demande) {
        return DemandeDTO.builder()
                .validite(demande.isValide())
                .demandeurDTO(demandeurService.convertToDTO(demande.getDemandeur()))
                .datedemande(demande.getDatedemande())
                .datetraitement(demande.getDatetraitement())
                .descriptiondemande(demande.getDescriptiondemande())
                .id(demande.getId())
                .numerodemande(demande.getNumerodemande())
                .objetdemande(demande.getObjetdemande())
                .statut(demande.getStatut())
                .urlattestation(demande.getUrlattestation())
                .attestaionName(demande.getAttestationName())
                .build();
    }

    /**
     * Convert dto demande to entity demande
     *
     * @param demande
     * @return
     */
    public Demande convertToEntity(DemandeDTO demande) {
        return Demande.builder()
                .validite(demande.isValidite())
                .datedemande(demande.getDatedemande())
                .datetraitement(demande.getDatetraitement())
                .descriptiondemande(demande.getDescriptiondemande())
                .id(demande.getId())
                .numerodemande(demande.getNumerodemande())
                .objetdemande(demande.getObjetdemande())
                .statut(demande.getStatut())
                .urlattestation(demande.getUrlattestation())
                .attestationName(demande.getAttestaionName())
                .demandeur(Demandeur.builder()
                        .id(demande.getDemandeurDTO().getId())
                        .build())
                .build();
    }

    /**
     * liste of demandes by status
     *
     * @param status
     * @return
     */

    @Override
    public List<DemandeDTO> findByStatut(String status) {
        return demandeRepository.findByStatut(status).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * get demande by Id demandeur
     *
     * @param id
     * @return
     */

    @Override
    public List<DemandeDTO> findByDemandeurId(int id) {
        return demandeRepository.findByDemandeurId(id).stream().map(this::convertToDTO).collect(Collectors.toList());
    }


}