package com.fonctionpublique.services.password;

import com.fonctionpublique.access.password.PasswordResetToken;
import com.fonctionpublique.entities.Utilisateur;

import java.util.Optional;

public interface PasswordService {
    void createPasswordResetTokenForUser(Utilisateur user, String passwordToken);

    String validatePasswordResetToken(String passwordResetToken);

    Optional<Utilisateur> findUserByPasswordToken(String passwordResetToken);

    PasswordResetToken findPasswordResetToken(String token);
}
