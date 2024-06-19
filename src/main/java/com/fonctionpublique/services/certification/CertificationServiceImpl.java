package com.fonctionpublique.services.certification;

import com.fonctionpublique.entities.*;
import com.fonctionpublique.repository.*;
import com.fonctionpublique.services.compteur.CompteurServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class CertificationServiceImpl implements  CertificationService{

    private final UtilisateurRepository utilisateurRepository;
    private final CertificationRepository certificationRepository;
    private final DemandeurRepository demandeurRepository;
    private final StructureRepository structureRepository;
    private final CompteurRepository compteurRepository;
    private static final String PREFIX = "Nº";
    //    public static void generatedQRCode(Utilisateur utilisateur) throws WriterException, IOException {
//
//        String qrCodePath = "/Users/7maksacodpc/Downloads/The_QRCode/";
//        String qrCodeName = qrCodePath + utilisateur.getFullName() + "-QRCODE.png";
//
//        var qrCodeWeriter = new QRCodeWriter();
//
//        BitMatrix bitMatrix = qrCodeWeriter.encode(
//                "ID:" + utilisateur.getId() +"\n"+
//                        "Nom:" + utilisateur.getFullName() + "\n" +
//                        "Email:" + utilisateur.getEmail(),
//                BarcodeFormat.QR_CODE, 400,400);
//
//        Path path = FileSystems.getDefault().getPath(qrCodeName);
//        MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
//
//
//    }
    public  String generatedQRCode(int id) throws WriterException, IOException {

        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);

        if(utilisateur.isPresent()) {

            Demandeur demandeur = utilisateur.get().getDemandeur();
            List<Demande> demande = utilisateur.get().getDemande();

            if(demandeur != null && demande != null) {

                String qrCodePath = "/Users/7maksacodpc/Downloads/The_QRCode/";
                String qrCodeName = qrCodePath + utilisateur.get().getFullName() + "-QRCODE.png";

                var qrCodeWriter = new QRCodeWriter();

                BitMatrix bitMatrix = qrCodeWriter.encode(
                        "ID:" + " "  + utilisateur.get().getId() + "\n" +
                                "Nom:" + " " + utilisateur.get().getFullName() + "\n" +
                                "Email:" + " " + utilisateur.get().getEmail() + "\n" +
                                "Source:" + " " + "Ministère de la fonction publique et de la Réforme du Secteur Public",
                        BarcodeFormat.QR_CODE, 120, 120);

                Path path = FileSystems.getDefault().getPath(qrCodeName);
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
                //System.out.println("the attestation link url is : " + demande.getUrlattestation());

                return qrCodeName;
            }else {
                return "demandeur ou demande introuvalble";
            }
        }else {
                return "id utilisateur introuvable";
        }
    }
    public Certification qRCode(Utilisateur utilisateur, Demande demande) throws WriterException, IOException {

        String qrCodePath = "/Users/7maksacodpc/Downloads/The_QRCode/";
        String qrCodeName = qrCodePath + UUID.randomUUID().toString() + "-QRCODE.png";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String baseURL = "https://localhost:4200/verificationattestation/";

        String fullURL = baseURL + demande.getId();

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
    public String generateAttestationNumber(int id) {
        Optional<Demandeur> IdDemandeur = demandeurRepository.findById(id);
        Optional<Structure> structure = structureRepository.findById(1);
        Optional<Compteur> compteur = compteurRepository.findById(1);
        return PREFIX + structure.get().getNatureAttestation() + "0000" + compteur.get().getCurrentCount() + "." + structure.get().getAbreviationNomStructure() + structure.get().getReference();
    }
//    public Certification qRCode(Utilisateur utilisateur, Demande demande) throws WriterException, IOException {
//        String qrCodePath = "/Users/7maksacodpc/Downloads/The_QRCode/";
//        String qrCodeName = qrCodePath + UUID.randomUUID().toString() + "-QRCODE.png";
//
//        var qrCodeWriter = new QRCodeWriter();
//
//        BitMatrix bitMatrix = qrCodeWriter.encode(
//                "ID:" + " "  + utilisateur.getId() + "\n" +
//                        "Nom:" + " " + utilisateur.getFullName() + "\n" +
//                        "Email:" + " " + utilisateur.getEmail() + "\n" +
//                        "Source:" + " " + "Ministère de la fonction publique et de la Réforme du Secteur Public",
//                BarcodeFormat.QR_CODE, 120, 120);
//
//        Path path = FileSystems.getDefault().getPath(qrCodeName);
//        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
//        Certification certification = new Certification();
//        certification.setCode(qrCodeName);
//        certification.setType("QRCODE");
//        certification.setDemande(demande);
//        //certificationRepository.save(certification);
//
//        return certificationRepository.save(certification);
//    }
}
// une table compteur
// q chque fois quon approuve une demande on lincremente
// initialiser a 1
// le 31 decembre de chaque annee on le reinitialise a 1
