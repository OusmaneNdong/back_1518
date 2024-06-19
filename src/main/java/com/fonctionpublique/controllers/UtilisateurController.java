package com.fonctionpublique.controllers;

import com.fonctionpublique.access.password.PasswordResetRequest;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.event.listenner.RegistrationCompleteEventListenner;
import com.fonctionpublique.mailing.PasswordMail;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.access.password.PasswordRequest;
import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.access.AuthenticationRequest;
import com.fonctionpublique.services.utilisateur.AuthenticationResponse;
import com.fonctionpublique.services.utilisateur.UtilisateurServiceImpl;
import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
@Slf4j
public class UtilisateurController {

    private final UtilisateurServiceImpl utilisateurServiceImpl;
    private final UtilisateurRepository utilisateurRepository;
    private final RegistrationCompleteEventListenner eventListener;
    private final PasswordMail passwordMail;

    @GetMapping("/utilisateurDetails/{id}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable int id){
        return  ResponseEntity.ok(utilisateurServiceImpl.getById(id));

    }
    @GetMapping("/statut/{statut}")
    public ResponseEntity<UtilisateurDTO> getByStatus(@PathVariable String statut){
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        Optional<Utilisateur> optionalUtilisateurOutilisateur = utilisateurServiceImpl.getByStatut(statut);
        if(optionalUtilisateurOutilisateur.isPresent()){
            Utilisateur utilisateur = optionalUtilisateurOutilisateur.get();
            BeanUtils.copyProperties(utilisateur,utilisateurDTO);
        }
        return ResponseEntity.ok(utilisateurDTO);
    }
    @PostMapping("/inscription")
    public ResponseEntity<String> inscription(@RequestBody UtilisateurDTO utilisateurDTO){
        boolean isCreated = utilisateurServiceImpl.creerUtilisateur(utilisateurDTO);
        if(isCreated){
            return  ResponseEntity.status(HttpStatus.CREATED).body("inscription réussie !");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email ou nin dèjá inscrie avec email" );
        }
    }
    @PostMapping("/register")
    public Utilisateur registration(@RequestBody RegistrationRequest registrationRequest) throws IOException, WriterException {
        return utilisateurServiceImpl.registerUtilisateur(registrationRequest);
    }
    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody UtilisateurDTO registrationRequest) {
            return utilisateurServiceImpl.connexion(registrationRequest);
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest registrationRequest) {
        return ResponseEntity.ok(utilisateurServiceImpl.authenticate(registrationRequest));
    }
//    @PostMapping("/connexion")
//    public ResponseEntity<?> connexion(@RequestBody RegistrationRequest registrationRequest){
//        try{
////            authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(registrationRequest, registrationRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(
//                    authenticationManager.authenticate(
//                            new UsernamePasswordAuthenticationToken(registrationRequest, registrationRequest.getPassword()))
//            );
//        }catch (AuthenticationException ex){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        UserDetails userDetails;
//        try{
//            userDetails = jwtUtilisateur.loadUserByUsername(registrationRequest.getEmail());
//        }catch (UsernameNotFoundException ex){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        String jwt = utilisateurUtil.generateToken(userDetails.getUsername());
//        return ResponseEntity.ok(jwt);
//    }
    @GetMapping("/getUtilisateur")
    public List<UtilisateurDTO> findAll() throws IOException, WriterException {

        return utilisateurServiceImpl.findAll();
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestBody PasswordRequest passwordRequest) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(passwordRequest.getEmail()).orElse(null);
        if (utilisateur == null) {
            utilisateur.setEmail(utilisateur.getEmail());
        }
        if (!utilisateurServiceImpl.oldPasswordIsValid(utilisateur, passwordRequest.getOldPassword())) {
            return "Incorrect old password";
        }
        String newPassword = passwordRequest.getNewPassword();
        if (newPassword == null) {
            return "New password cannot be null";
        }

        utilisateurServiceImpl.changePassword(utilisateur, newPassword);
        return "Password changed successfully";
    }


    @PostMapping("/change-password-test")
    public UtilisateurDTO changePasswordTest(@RequestBody PasswordRequest passwordRequest) {
            return utilisateurServiceImpl.changePassword(passwordRequest.getEmail(), passwordRequest.getNewPassword());
    }

    @PutMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestParam String email) {
        return ResponseEntity.ok(utilisateurServiceImpl.forgetPassword(email));
    }
//@PutMapping("/forget-password")
//public  ResponseEntity<PasswordResetToken> forgetPassword(@RequestBody PasswordResetRequest passwordResetRequest){
//    return ResponseEntity.ok(utilisateurServiceImpl.forgetPassword(passwordResetRequest));
//}
   @PutMapping("/set-password")
   public UtilisateurDTO setPassword(@RequestBody PasswordResetRequest passwordResetRequest){
        return  utilisateurServiceImpl.setPassword(passwordResetRequest.getEmail(),passwordResetRequest.getNewPassword());
   }
    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest,
                                       final HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {

        Optional<Utilisateur> user = utilisateurRepository.findByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";
        if (user.isPresent()) {
            String passwordResetToken = UUID.randomUUID().toString();
            //PasswordResetToken pwdt = new PasswordResetToken(passwordResetToken, user.get());
            //pwdt.setToken(passwordResetToken);
            //pwdt.setUser(user.get());
            //passwordResetTokenRepository.save(pwdt);
//System.out.println("token="+pwdt);
            utilisateurServiceImpl.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            //passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(request), passwordResetToken);
                passwordMail.sentSetPasswordEmail(passwordResetRequest.getEmail(), passwordResetToken, user.get().getFullName());
        }
        return passwordResetUrl;
    }
    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"
                +request.getServerPort()+request.getContextPath();
    }
    private String passwordResetEmailLink(Utilisateur user, String applicationUrl,
                                          String passwordToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/register/reset-password?token="+passwordToken;
        eventListener.sendPasswordResetVerificationEmail(url);
        log.info("Click the link to reset your password :  {}", url);
        return url;
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest passwordRequestUtil,
                                @RequestParam("token") String token){
        String tokenVerificationResult = utilisateurServiceImpl.validatePasswordResetToken(token);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")) {
            return "Invalid token password reset token";
        }
        Optional<Utilisateur> theUser = Optional.ofNullable(utilisateurServiceImpl.findUserByPasswordToken(token));
        if (theUser.isPresent()) {
            utilisateurServiceImpl.resetUtilisateurPassword(theUser.get(), passwordRequestUtil.getNewPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }


}
