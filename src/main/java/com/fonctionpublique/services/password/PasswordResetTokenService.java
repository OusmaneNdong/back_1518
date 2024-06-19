package com.fonctionpublique.services.password;

import com.fonctionpublique.access.password.PasswordResetToken;
import com.fonctionpublique.repository.PasswordResetTokenRepository;
import com.fonctionpublique.entities.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService implements PasswordService{

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    @Override
    public void createPasswordResetTokenForUser(Utilisateur user, String passwordToken) {
        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordRestToken);
    }
    @Override
    public String validatePasswordResetToken(String passwordResetToken) {
        PasswordResetToken passwordToken = passwordResetTokenRepository.findByToken(passwordResetToken);
        if (passwordToken == null) {
            return "Invalid verification token";
        }
        Utilisateur user = passwordToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((passwordToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return "Link already expired, resend link";
        }
        return "valid";
    }
    @Override
    public Optional<Utilisateur> findUserByPasswordToken(String passwordResetToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordResetToken).getUser());
    }
    @Override
    public PasswordResetToken findPasswordResetToken(String token){
        return passwordResetTokenRepository.findByToken(token);
    }
}
