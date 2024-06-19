package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.services.dashbord.DashbordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/dashbord")
@RequiredArgsConstructor
public class DashbordController {

        private final DashbordService dashbordService;
    @GetMapping("/total")
    public ResponseEntity<Integer> getCount(){
        return dashbordService.getCount();
    }

    @GetMapping("/total-cours")
    public ResponseEntity<Integer> getCours(){
        return dashbordService.getCours();
    }

    @GetMapping("/total-approuvees")
    public ResponseEntity<Integer> getAppouved(){
        return dashbordService.getApprouved();
    }

    @GetMapping("/total-rejetees")
    public ResponseEntity<Integer> getRejected(){

        return dashbordService.getRejected();
    }



    @GetMapping("/demande/total")
    public ResponseEntity<List<DemandeDTO>> getdemandeCount(){
        return dashbordService.getDemandeCount();
    }

    @GetMapping("/demande/total-cours")
    public ResponseEntity<List<DemandeDTO>> getdemandeCours(){
        return dashbordService.getDemandeCours();
    }

    @GetMapping("/demande/total-approuvees")
    public ResponseEntity<List<DemandeDTO>> getDemandeAppouved(){
        return dashbordService.getDemandeApprouved();
    }

    @GetMapping("/demande/total-rejetees")
    public ResponseEntity<List<DemandeDTO>> getDemandeRejected(){

        return dashbordService.getDemandeRejected();
    }

}
