package com.fonctionpublique.repository;

import com.fonctionpublique.access.password.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String passwordResetToken);
}
