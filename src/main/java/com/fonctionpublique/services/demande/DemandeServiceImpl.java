package com.fonctionpublique.services.demande;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Certification;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.repository.CertificationRepository;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.services.certification.CertificationServiceImpl;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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


    @Override
    public List<DemandeDTO> findAll() {
        return demandeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override
    public DemandeDTO getById(int id) {
        Optional<Demande> demande = demandeRepository.findById(id);
        if (demande.isPresent()){
            return convertToDTO(demande.get());
        }
        return null ;
    }
    @Override
    public Integer creerDemande(Demande demande,int id) throws IOException, WriterException {

        Optional<Demandeur> demandeur = demandeurRepository.findById(id);

        if (isValidDemande(demande)) {
            if (demandeur.isPresent()) {
                demande.setDemandeur(demandeur.get());
                demande.setNumerodemande(certificationServiceImpl.generateAttestationNumber(id));
                demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
                demande.setDatedemande(LocalDateTime.now());
                demande.setDatetraitement(LocalDateTime.now());
                demande.setValidite(true);
                demande.setObjetdemande("Demande de non-appartenance á la Fonction Publique");
                demande.setDescriptiondemande("Description de la demande");

                return demandeRepository.save(demande).getId();
            } else {
                return null;
            }
        }
        return null;
    }

    public Boolean isValidDemande(Demande demande){
        if(demande.getDatedemande()!=null && demande.getNin() != null && demande.getNumerodemande()!=null && demande.getStatut() != null){
            return false;
        }
        return true;
    }
    public DemandeDTO convertToDTO(Demande demande){
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
    @Override
    public List<DemandeDTO> findAllDemande() throws IOException, WriterException {

        List<DemandeDTO> DTO = demandeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
        if (DTO.size() != 0) {
            for (DemandeDTO dto : DTO) {
                Utilisateur utils = new Utilisateur();
                BeanUtils.copyProperties(dto, utils);
                certificationServiceImpl.generatedQRCode(utils.getId());

                Certification certification = new Certification();
                certification.setCode(certificationServiceImpl.generatedQRCode(utils.getId()));
                certification.setType("QRCode");

                certificationRepository.save(certification);

                Optional<Demande> optionalDemande = demandeRepository.findById(dto.getId());
                Optional<Certification> optionalCertification = certificationRepository.findById(certification.getId());
                if(optionalDemande.isPresent()){

                    optionalDemande.get().setCertification(certification);
                    demandeRepository.save(optionalDemande.get());

                    optionalCertification.get().setDemande(optionalDemande.get());
                    certificationRepository.save(optionalCertification.get());

                }
                return demandeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
            }
        }
        return DTO;
    }

    @Override
    public List<DemandeDTO> findByStatut(String status) {
        return demandeRepository.findByStatut(status).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<DemandeDTO> findByDemandeurId(int id){
        return demandeRepository.findByDemandeurId(id).stream().map(this::convertToDTO).collect(Collectors.toList());
    }


}