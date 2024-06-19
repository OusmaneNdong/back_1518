package com.fonctionpublique.services.utilisateur;

import com.fonctionpublique.access.AuthenticationRequest;
import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.access.token.VerificationToken;
import com.fonctionpublique.access.token.VerificationTokenRepository;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Certification;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Profile;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.handleException.EmailAlreadyExistException;
import com.fonctionpublique.handleException.NinAlreadyExistException;
import com.fonctionpublique.mailing.InscriptionMailSender;
import com.fonctionpublique.mailing.PasswordMail;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.ProfileRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.security.JwtUtilisateur;
import com.fonctionpublique.services.certification.CertificationServiceImpl;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import com.fonctionpublique.services.password.PasswordResetTokenService;
import com.fonctionpublique.utils.UtilisateurUtil;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Slf4j
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {
    
    private final VerificationTokenRepository tokenRepository;
    private final PasswordMail passwordMail;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final DemandeurRepository demandeurRepository;
    private final ProfileRepository profileRepository;
    private final InscriptionMailSender inscriptionMailSender;
    private final JwtUtilisateur jwtUtilisateur;
    private final UtilisateurUtil utilisateurUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenService passwordResetTokenService;
    private final CertificationServiceImpl certificationServiceImpl;
    private final DemandeurServiceImpl demandeurService;

    @Override
    public UtilisateurDTO getById(int id) {
        return convertDTO(utilisateurRepository.findById(id).orElse(null));
    }
    private UtilisateurDTO convertToDTO(Utilisateur utilisateur){

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setPassword(utilisateur.getPassword());
        utilisateurDTO.setNin(utilisateur.getNin());
        utilisateurDTO.setPassPort(utilisateur.getPassPort());
        return utilisateurDTO;
    }
    @Override
    public void roleExisting() {

        Profile adminRole = new Profile();
        adminRole.setCode("Admin");
        adminRole.setLibelle("Admin Profile");
        adminRole.setEtat("true");
        profileRepository.save(adminRole);

        Profile userRole = new Profile();
        userRole.setCode("user");
        userRole.setLibelle("Role par default de l'utilisateur");
        userRole.setEtat("true");
        profileRepository.save(userRole);
    }
    @Override
    public boolean creerUtilisateur(UtilisateurDTO utilisateurDTO) {

        if (utilisateurRepository.existsByEmail(utilisateurDTO.getEmail())) {
            return false;
        }
        if (utilisateurRepository.existsByNin(utilisateurDTO.getNin())) {
            return false;
        }

        Utilisateur utilisateur = new Utilisateur();
        BeanUtils.copyProperties(utilisateurDTO, utilisateur);

        String hashPassword = passwordEncoder.encode(utilisateurDTO.getPassword());
        utilisateur.setPassword(hashPassword);

        utilisateur.setStatut("true");

        Profile profile = findOrCreateProfile("user");
        utilisateur.setProfile(profile);

//        inscriptionMailSender.sendMailToAllAdmin(utilisateur.isStatus(), utilisateur.getEmail(), utilisateurRepository.getAllAdmin());

        utilisateurRepository.save(utilisateur);

        Demandeur demandeur = new Demandeur();
        demandeur.setUtilisateur(utilisateur);
        demandeur.setNin(utilisateur.getNin());
        demandeurRepository.save(demandeur);
        utilisateur.setDemandeur(demandeur);
        return true;
    }
    @Override
    public Optional<Utilisateur> findByUtilisateur(int id) {
        return Optional.empty();
    }
    @Override
    public Optional<Utilisateur> findByUtilisateur(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    public UtilisateurDTO convertDTO(Utilisateur utilisateur){

        return UtilisateurDTO.builder()
                .id(utilisateur.getId())
                .prenom(utilisateur.getPrenom())
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .password(utilisateur.getPassword())
                .nin(utilisateur.getNin())
                .statut(utilisateur.getStatut())
                .fullName(utilisateur.getFullName())
                .demandeurDTO(demandeurService.convertToDTO(utilisateur.getDemandeur()))
                .signature(utilisateur.getSignature())
                .passPort(utilisateur.getPassPort())
                .typePieces(utilisateur.getTypePieces())
                .build();
    }
    private UtilisateurDTO convertUtilisateurToDTO(Utilisateur utilisateur) {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();

        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setPassword(utilisateur.getPassword());
        utilisateurDTO.setNin(utilisateur.getNin());
        utilisateurDTO.setStatut(utilisateur.getStatut());
        utilisateurDTO.setPassword(utilisateur.getSignature());

        return utilisateurDTO;
    }
    @Override
    public List<UtilisateurDTO> findAll() throws IOException, WriterException {
       return utilisateurRepository.findAll().stream().map(this::convertDTO).collect(Collectors.toList());
    }
    private Profile findOrCreateProfile(String profileName) {
        Profile profile = profileRepository.findByCode(profileName)
                .orElse(null);
        if (profile == null) {
            return profileRepository.save(
                    Profile.builder()
                            .libelle(" Role par default de l'utilisateur")
                            .code(profileName)
                            .etat("true")
                            .build());
        }
        return profile;
    }
    @Override
    public Utilisateur registerUtilisateur(RegistrationRequest registrationRequest) throws IOException, WriterException {
        if (utilisateurRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistException("email exist");
        }
        if (utilisateurRepository.existsByNin(registrationRequest.getNin())) {
            throw new NinAlreadyExistException("nin exist");
        }

        Utilisateur utilisateur = new Utilisateur();
        String hashPassword = passwordEncoder.encode(registrationRequest.getPassword());
        utilisateur.setPassword(hashPassword);

//        String civilite = demandeur.getSexe().equalsIgnoreCase("Masculin") ? "Monsieur" : "Madame";

        String identityNin = "^[e0-9]{10,10}$";
        String identityPassport = "^[e0-9]{10,10}$";
//        String identityNinOrPassport = utilisateur.getTypePieces().equalsIgnoreCase("nin") ? identityNin : identityPassport;



        Profile profile = findOrCreateProfile("user");
        utilisateur.setNin(registrationRequest.getNin());
        utilisateur.setEmail(registrationRequest.getEmail());
        utilisateur.setNom(registrationRequest.getNom());
        utilisateur.setPrenom(registrationRequest.getPrenom());
        utilisateur.setStatut("true");
//        utilisateur.setTypePieces(identityNinOrPassport);
//      utilisateur.setPassePort(registrationRequest.getPassePort());
        utilisateur.setProfile(profile);

        utilisateurRepository.save(utilisateur);
        List<UtilisateurDTO> DTO = utilisateurRepository.findAll().stream().map(this::convertUtilisateurToDTO).collect(Collectors.toList());
        if( DTO.size() != 0){
            for(UtilisateurDTO dto : DTO){
                Utilisateur utils = new Utilisateur();
                BeanUtils.copyProperties(dto,utils);
                certificationServiceImpl.generatedQRCode(utils.getId());

                Certification certification = new Certification();
                certification.setCode(certificationServiceImpl.generatedQRCode(dto.getId()));
                certification.setType("QRCODE");

            }
        }

//        Utilisateur utilisateur1 = new Utilisateur(1,"ousmane","Ndong","ndongmafale10@gmail.com","passer","1570197700123","true",null,null,profile);



        inscriptionMailSender.sendMailToAllAdmin(registrationRequest.getStatus(), registrationRequest.getEmail(), utilisateurRepository.getAllAdmin());


            return utilisateur;
    }
    @Override
    public ResponseEntity<?> connexion(UtilisateurDTO registrationRequest) {
        Optional<Utilisateur> u = utilisateurRepository.findByEmail(registrationRequest.getEmail());
        String cni="";
        if (u.isPresent()){
            cni = u.get().getNin();
        }        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), registrationRequest.getPassword()));
           if(auth.isAuthenticated()) {
               UserDetails userDetails = jwtUtilisateur.loadUserByUsername(registrationRequest.getEmail());
               String token = utilisateurUtil.generateToken(userDetails.getUsername(),cni);
               return ResponseEntity.ok(token);
           }
        } catch (Exception ex) {
            log.error("Error occurred during authentication: {}", ex.getMessage());
        }
        return ResponseEntity.badRequest().body("Authentication failed");
    }
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest registrationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), registrationRequest.getPassword())
        );
        final Utilisateur user = utilisateurRepository.findByEmail(registrationRequest.getEmail()).get();
        UserDetails userDetails = jwtUtilisateur.loadUserByUsername(registrationRequest.getEmail());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("nin", user.getNin());
        claims.put("fullName", user.getPrenom()+" "+user.getNom());
        claims.put("profile",user.getProfile().getCode());
        String token = utilisateurUtil.generateToken(userDetails.getUsername(),claims);

        return AuthenticationResponse.builder()
                        .token(token)
                        .build();


    }
    @Override
    public void saveUserVerificationToken(Utilisateur user, String token) {
        var verificationToken = new VerificationToken(token, user);
        tokenRepository.save(verificationToken);
    }
    @Override
    public void changePassword(Utilisateur user, String newPassword) {
        if (newPassword != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            utilisateurRepository.save(user);
        } else {
            throw new IllegalArgumentException("New password cannot be null");
        }
    }
    @Override
    public boolean oldPasswordIsValid(Utilisateur utilisateur, String oldPassword) {
        return  passwordEncoder.matches(oldPassword,utilisateur.getPassword());
    }
    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        Utilisateur user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token already expired";
        }
        utilisateurRepository.save(user);
        return "valid";
    }
    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }
    @Override
    public Optional<Utilisateur> getByStatut(String statut) {
            return utilisateurRepository.findByStatut(statut);
    }
    @Override
    public void createPasswordResetTokenForUser(Utilisateur utilisateur, String passwordResetToken) {
            passwordResetTokenService.createPasswordResetTokenForUser(utilisateur, passwordResetToken);
    }
    @Override
    public String validatePasswordResetToken(String token) {
        return passwordResetTokenService.validatePasswordResetToken(token);
    }
    @Override
    public Utilisateur findUserByPasswordToken(String token) {
        return passwordResetTokenService.findUserByPasswordToken(token).get();
    }
    public void resetUtilisateurPassword(Utilisateur user,String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(user);
    }
    @Override
    public UtilisateurDTO changePassword(String email, String newPassword) {
        Utilisateur u = utilisateurRepository.findByEmail(email).orElse(null);
        if (u !=null){
            u.setPassword(passwordEncoder.encode(newPassword));
           u= utilisateurRepository.save(u);
        }
        UtilisateurDTO uDTO = new UtilisateurDTO();
        BeanUtils.copyProperties(u,uDTO);
        return uDTO;
    }
    @Override
    public String forgetPassword(String email) {
        Utilisateur user =  utilisateurRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("email introuvable" + email));
        passwordMail.sentSetPasswordEmail(email);

        Map<String,Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        String token = utilisateurUtil.generateToken(email,claims);
        return token;
    }
     @Override
    public UtilisateurDTO setPassword(String email, String newPassword) {
         Utilisateur user =  utilisateurRepository.findByEmail(email)
                 .orElseThrow(
                         () -> new RuntimeException("email introuvable" + email));
         user.setPassword(passwordEncoder.encode(newPassword));
         utilisateurRepository.save(user);
         UtilisateurDTO dto = new UtilisateurDTO();
         BeanUtils.copyProperties(user,dto);
         return dto;
    }
}






