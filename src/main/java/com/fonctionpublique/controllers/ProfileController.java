package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.ProfileDTO;
import com.fonctionpublique.services.profile.ProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
//@RequestMapping("/api/profile")
public class ProfileController {

//    private final ProfileServiceImpl profileServiceImpl;
//    @Autowired
//    public ProfileController(ProfileServiceImpl profileServiceImpl) {
//        this.profileServiceImpl = profileServiceImpl;
//    }

//    @PostMapping({"/createNewRole"})
//    public boolean createNewRole(@RequestBody ProfileDTO profileDTO) {
//        profileServiceImpl.createNewRole(profileDTO);
//        return true;
//    @GetMapping("/getProfile")
//    public List<ProfileDTO> findAll(){
//        return profileServiceImpl.findAll();
//    }

//    }
}
