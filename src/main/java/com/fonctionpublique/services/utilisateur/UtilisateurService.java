package com.fonctionpublique.services.utilisateur;

import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.access.AuthenticationRequest;
import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UtilisateurService {
    boolean creerUtilisateur(UtilisateurDTO utilisateurDTO);
    Optional<Utilisateur> findByUtilisateur(int id);
    Optional<Utilisateur> findByUtilisateur(String email);
    List<UtilisateurDTO> findAll() throws IOException, WriterException;

    UtilisateurDTO getById(int id);

    Optional<Utilisateur> getByStatut(String statut);

    public void roleExisting();

    Utilisateur registerUtilisateur(RegistrationRequest registrationRequest) throws IOException, WriterException;

//    Utilisateur olDregisterUtilisateur(RegistrationRequest registrationRequest);

    ResponseEntity<?> connexion(UtilisateurDTO registrationRequest);
    //VerificationToken authenticate(AuthenticationRequest registrationRequest);
    AuthenticationResponse authenticate(AuthenticationRequest registrationRequest);

    //    @Override
    //    public PasswordResetToken forgetPassword(PasswordResetRequest passwordResetRequest) {
    //        Utilisateur user = utilisateurRepository.findByEmail(passwordResetRequest.getEmail())
    //                .orElseThrow(
    //                        ()-> new RuntimeException("email introuble" + passwordResetRequest.getEmail())
    //                );
    //
    //            PasswordResetToken passwordResetToken = null;
    //            Map<String, Object> claims = new HashMap<>();
    //            claims.put("email", passwordResetRequest.getEmail());
    //            claims.put("token_id",passwordResetToken.getToken_id());
    //            claims.put("expiration_token", passwordResetToken.getTokenExpirationTime());
    //            String theToken = utilisateurUtil.generateToken(passwordResetRequest.getEmail(),claims);
    //            passwordMail.sentSetPasswordEmail(passwordResetRequest.getEmail());
    //
    //        return PasswordResetToken.builder()
    //                .token(theToken)
    //                .build();
    //
    //    }
    String forgetPassword(String email);

    void saveUserVerificationToken(Utilisateur user, String token);

    void changePassword(Utilisateur user, String newPassword);

    boolean oldPasswordIsValid(Utilisateur utilisateur, String oldPassword);


    String validateToken(String theToken);

    Optional<Utilisateur> findByEmail(String email);

    Utilisateur findUserByPasswordToken(String token);

    void createPasswordResetTokenForUser(Utilisateur utilisateur, String passwordResetToken);

    String validatePasswordResetToken(String token);

    UtilisateurDTO changePassword(String email, String newPassword);

//    PasswordResetToken forgetPassword(PasswordResetRequest passwordResetRequest);

    UtilisateurDTO setPassword(String email, String newPassword);
}
