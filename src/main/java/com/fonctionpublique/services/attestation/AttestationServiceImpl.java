package com.fonctionpublique.services.attestation;

import com.fonctionpublique.entities.*;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.services.certification.CertificationServiceImpl;
import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttestationServiceImpl implements  AttestationService{

    private  final DemandeRepository demandeRepository;
    private  final CertificationServiceImpl certificationService;
    private final DemandeurRepository demandeurRepository;

    @Override
    public String getAttestationPdf(Utilisateur utilisateur, Demandeur demandeur, Demande demande, Structure structure) throws IOException, WriterException {

        String civilite = demandeur.getSexe().equalsIgnoreCase("Masculin") ? "Monsieur" : "Madame";
        String naissance = demandeur.getSexe().equalsIgnoreCase("Masculin") ? "né" : "née";
        String inconnue = demandeur.getSexe().equalsIgnoreCase("Masculin") ? "inconnu" : "inconnue";
        String attestationName = genCode()+".pdf";
        String path = "/Users/7maksacodpc/Downloads/attestations/"+attestationName;

        String titre = "ATTESTATION";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy" + "  " + " à " + " " + "HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);

        String data = "générée le " + formattedDateTime;



        String numeroAttestation = certificationService.generateAttestationNumber(demandeur.getId());

        String parath1 = "REPUBLIQUE DU SENEGAL";
        String para1th = "Un Peuple-Un But-Une Foi";
        String parth1etoil = "**********";

        String parath2 = structure.getNomStructure();
        String parath2etoil = "**********";

        String parath3 = "Direction générale de la  fonction Publique";




        String parath = "Le Directeur général de la Fonction publique,soussigné, atteste que, " +
                civilite+ "\n"+ " " + " " + utilisateur.getFullName().toUpperCase() + " " + naissance + " " + "le" + " " + " " + demandeur.getDatedenaissance().toUpperCase()+ " " + "á" +" " + demandeur.getLieudenaissance().toUpperCase() + " " +
                "est"+ " " + inconnue + " " + "au Fichier de la Fonction publique." + "\n" + "En foi de quoi, la présente attestation lui est délivrée pour servir et valoir ce que de droit";


        String parath4 = "NB: l'attestation délivrée est valable pour une durée de neuf (9) mois.";

        String parath5 = structure.getLocalisation() + " " + "Contact :" + " " + structure.getContact() + " " +"\n"+
                        " " + "BP :" + " " + structure.getBoitePostale() + " " + " " + "Email :" + structure.getEmail();


        String urlDrapeu = "/Users/7maksacodpc/Downloads/logo-drapeua-du-senegal.png";
        ImageData drapeau = ImageDataFactory.create(urlDrapeu);
        Image imageDrapeau = new Image(drapeau);
        imageDrapeau.setRelativePosition(25,0,0,0);
        imageDrapeau.setWidth(100);
        imageDrapeau.setHeight(25);

        String urlLogoMinister = "/Users/7maksacodpc/Downloads/logominister.png";
        ImageData logoMinistere = ImageDataFactory.create(urlLogoMinister);
        Image imageLogoMinister = new Image(logoMinistere);
        imageLogoMinister.setRelativePosition(55,-30,0,0);
        imageLogoMinister.setWidth(50);
        imageLogoMinister.setHeight(50);


        //String urlcachet = "/Users/7maksacodpc/Downloads/cachet_sane.png";
        String urlcachet = utilisateur.getSignature();// "/Users/7maksacodpc/Downloads/cachet_sane.png";
            ImageData cachet = ImageDataFactory.create(urlcachet);
            Image imageCachet = new Image(cachet);
            imageCachet.setWidth(130);
            imageCachet.setHeight(140);
            imageCachet.setRelativePosition(340, 35, 0, 0);

        Certification certification = certificationService.qRCode(utilisateur,demande);
        String imFile = certification.getCode();
        ImageData qR = ImageDataFactory.create(imFile);
        Image imageQR = new Image(qR);
        imageQR.setRelativePosition(0,160,0,0);

        demande.setUrlattestation(path);
        demande.setAttestationName(attestationName);
        demande.setDatetraitement(LocalDateTime.now());
        demande.setStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
        demande.setUtilisateur(utilisateur);
        demande.setCertification(certification);
        demande.setDemandeur(demandeur);

        demandeRepository.save(demande);

        demandeur.setStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
        demandeurRepository.save(demandeur);





        String urlDrapeaulineaire = "/Users/7maksacodpc/Downloads/logo-drapeua-du-senegal.png";
        ImageData drapeauLineaire = ImageDataFactory.create(urlDrapeaulineaire);
        Image imageDrapeauLineaire = new Image(drapeauLineaire);
        imageDrapeauLineaire.setWidth(500);
        imageDrapeauLineaire.setHeight(3);
        imageDrapeauLineaire.setRelativePosition(0,83,0,0);

        Paragraph paraDate = new Paragraph(data);
        paraDate.setRelativePosition(280,-90,0,0);
        Paragraph paraNumeroAttestation = new Paragraph(numeroAttestation);
        paraNumeroAttestation.setFontSize(10);
        paraNumeroAttestation.setRelativePosition(260,-70,0,0);

        Paragraph para1 = new Paragraph(parath1).setFontSize(12);
        Paragraph para11 = new Paragraph(para1th).setFontSize(12);
        para11.setRelativePosition(25,-10,0,0).setFontSize(8);
        Paragraph para1etoil = new Paragraph(parth1etoil);
        para1etoil.setRelativePosition(45,-20,0,0);

        Paragraph para2 = new Paragraph(parath2).setFontSize(12).setRelativePosition(0,-28,0,0);
        Paragraph para2etoil = new Paragraph(parath2etoil).setFontSize(12);
        para2etoil.setRelativePosition(65,-35,0,0);

        Paragraph para3 = new Paragraph(parath3).setFontSize(13).setItalic();
        para3.setRelativePosition(0,-15,0,0);

        Paragraph title = new Paragraph(titre).setFontSize(20).setBold().setBackgroundColor(Color.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER);
        title.setRelativePosition(0,20,0,0);
        title.setBorder(new SolidBorder(1));

        Paragraph para = new Paragraph(parath).setFontSize(11);
        para.setRelativePosition(0,50,0,0);

        Paragraph para4nb = new Paragraph(parath4).setFontSize(10);
        para4nb.setRelativePosition(80,60,0,0);

        Paragraph para5footer = new Paragraph(parath5).setFontSize(8);
        para5footer.setRelativePosition(80,85,0,0);

        PdfWriter writer = new PdfWriter(path);

        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.addNewPage();

        Document document = new Document(pdfDocument);

        document.add(imageDrapeau);

        document.add(para1);
        document.add(para11);
        document.add(para1etoil);

        document.add(paraNumeroAttestation);

        document.add(imageLogoMinister);

        document.add(para2);
        document.add(para2etoil);
        document.add(paraDate);

        document.add(para3);

        document.add(title);

        document.add(para);

        document.add(imageQR);
        document.add(imageCachet);

        document.add(para4nb);

        document.add(imageDrapeauLineaire);

        document.add(para5footer);


        document.close();

        System.out.println("fichier pdf généré");

        return path;
    }

    public String genCode(){

        return UUID.randomUUID().toString();
    }

    public boolean creerAttestation(Demande demande) {
        demande.setNumerodemande(genCode());
        demande.setDatetraitement(LocalDateTime.now());
        demande.setStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
        demande.setValidite(true);
            demandeRepository.save(demande);
            return true;
    }
}
