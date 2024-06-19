package com.fonctionpublique.services.profile;

import com.fonctionpublique.entities.Profile;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.repository.ProfileRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void roleExisting() {

        int num = 1;

        profileRepository.findAllById(num).orElseGet(() -> {
            Profile adminProfile = new Profile();
            adminProfile.setCode("admin");
            adminProfile.setLibelle("admininstrateur");
            adminProfile.setEtat("true");
            profileRepository.save(adminProfile);
            return null;
        });
    }
    @PostConstruct
    public void adminExisting() {

        Optional<Profile> admin = profileRepository.findByCode("admin");
        if (admin.isPresent()) {
            Profile adminProfile = admin.get();

            int num = 1;
            utilisateurRepository.findById(num).orElseGet(() -> {

                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setPrenom("Ousmane");
                utilisateur.setNom("ndong");
                utilisateur.setEmail("admin@gmail.com");
                utilisateur.setPassword(passwordEncoder.encode("passer"));
                utilisateur.setNin("1570199800234");
                utilisateur.setStatut("true");
                utilisateur.setSignature("/Users/7maksacodpc/Downloads/cachet_sane.png");
                utilisateur.setProfile(adminProfile);
                utilisateurRepository.save(utilisateur);

                return null;

            });
        }
    }
}
