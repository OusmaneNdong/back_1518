package com.fonctionpublique.services.certification;

import com.fonctionpublique.entities.*;
import com.fonctionpublique.repository.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CertificationServiceImpl implements CertificationService {

    private final UtilisateurRepository utilisateurRepository;
    private final CertificationRepository certificationRepository;
    private final DemandeurRepository demandeurRepository;
    private final StructureRepository structureRepository;
    private final CompteurRepository compteurRepository;


    @Override
    public Certification qRCode(Utilisateur utilisateur, Demande demande) throws WriterException, IOException {

        String qrCodeName = Params.DIRECTORYQRCOD + UUID.randomUUID().toString() + "-QRCODE.png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String fullURL = Params.LIENDEVERIFICATION + demande.getId();

        Map<EncodeHintType, Object> qrCodeParams = new HashMap<>();
        qrCodeParams.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        qrCodeParams.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.toString());
        qrCodeParams.put(EncodeHintType.MARGIN, 2);

        BitMatrix bitMatrix = qrCodeWriter.encode(fullURL, BarcodeFormat.QR_CODE, 120, 120, qrCodeParams);

        Path path = FileSystems.getDefault().getPath(qrCodeName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        Certification certification = new Certification();
        certification.setCode(qrCodeName);
        certification.setType("QRCODE");
        certification.setDemande(demande);

        return certificationRepository.save(certification);
    }

    @Override
    public String generateAttestationNumber(int id) {
        Optional<Demandeur> IdDemandeur = demandeurRepository.findById(id);
        Optional<Structure> structure = structureRepository.findById(1);
        Optional<Compteur> compteur = compteurRepository.findById(1);
        return Params.PREFIX + structure.get().getNatureAttestation() + "0000" + compteur.get().getCurrentCount()
                + "." + structure.get().getAbreviationNomStructure()
                + structure.get().getReference();
    }

}

