package com.fonctionpublique.event.listenner;

import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.event.RegistrationCompleteEvent;
import com.fonctionpublique.services.utilisateur.UtilisateurServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListenner implements ApplicationListener<RegistrationCompleteEvent> {


    private final UtilisateurServiceImpl utilisateurServiceImpl;
    private final JavaMailSender mailSender;
    private Utilisateur user;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        user = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        utilisateurServiceImpl.saveUserVerificationToken(user,verificationToken);
        String url = event.getApplicationUrl() + "/api/utilisateur/register/verifyEmail?token=" + verificationToken;
        try {
            sendPasswordResetVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click sur ce lien pour pour réinitialiser votre mot de passe {}", url );
    }

    public void sendPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        System.out.println("url="+url);
        String subject = "Réinitialisation mot de passes";
        String senderName = "Ministere de la fonction publique";
        String mailContent = "<p> Hi, "+ user.getPrenom()+ " " + user.getNom()+ ", </p>"+
                "<p><b>vous souhaitez réinitialiser votre mot de passe,</b>"+"" +
                "veillez cliquer sur ce lien pour complèter votre action.</p>"+
                "<a href=\"" +url+ "\">je crée un nouveau mon mot de passe</a>"+
                "<p> Ministère de la fonction publique.";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("ndongmafale10@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}
