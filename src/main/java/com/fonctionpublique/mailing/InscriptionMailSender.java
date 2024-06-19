package com.fonctionpublique.mailing;

import com.fonctionpublique.filter.UtilisateurFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InscriptionMailSender {

    private final InscriptionMail inscriptionMail;
    private final UtilisateurFilter utilisateurFilter;
    @Autowired
    public InscriptionMailSender(InscriptionMail inscriptionMail, UtilisateurFilter utilisateurFilter) {
        this.inscriptionMail = inscriptionMail;
        this.utilisateurFilter = utilisateurFilter;
    }

    public void sendMailToAllAdmin(String status, String user, List<String> allAdmin){
        allAdmin.remove(utilisateurFilter.getCurrentUser());
        if(status != null && status.equalsIgnoreCase("true")){
            inscriptionMail.sendSimpleMessage(
                    utilisateurFilter.getCurrentUser(),
                    "Bonjour Mr/Mme,\n" + user +
                            "Votre inscription à la plateforme de demande d'attestation de non-appartenance à la fonctin publique \n" +
                            "a été éffectuée avec success.\n" +
                            "Veillez vous connectez grâce á ce lien: \n" +
                            ""
                            + user + " " + " et mot de passe pour continuer.",
                    "User:- " + user + "\n Test the mailing application work if you received this mail, thanks ! \n Admin:- " + utilisateurFilter.getCurrentUser(),allAdmin);
        }else{
            inscriptionMail.sendSimpleMessage(
                    utilisateurFilter.getCurrentUser(),
                    "Account Disable",
                    "User:- " + user + "\n is disable by \n Admin:- " + utilisateurFilter.getCurrentUser(),allAdmin);
        }

    }

}
