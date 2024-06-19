package com.fonctionpublique.services.attestation;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.entities.Utilisateur;
import com.google.zxing.WriterException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface AttestationService {
//    void getAttestationPdf(String email) throws IOException;

//    void getAttestationPdf(Utilisateur utilisateur , Demandeur demandeur) throws IOException, WriterException;

    String getAttestationPdf(Utilisateur utilisateur , Demandeur demandeur, Demande demande, Structure structure) throws IOException, WriterException;
}
