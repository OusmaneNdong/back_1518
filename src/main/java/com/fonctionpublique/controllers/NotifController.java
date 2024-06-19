package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@CrossOrigin
public class NotifController {
    @Autowired
    private JavaMailSender javaMailSender;
    @PostMapping("")
    public String sendEmail(@RequestBody EmailDTO emailDTO){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailDTO.getTo());
        simpleMailMessage.setSubject(emailDTO.getSubject());
        simpleMailMessage.setText(emailDTO.getText());

        javaMailSender.send(simpleMailMessage);

        return "Email sent succesfully";
    }
}
