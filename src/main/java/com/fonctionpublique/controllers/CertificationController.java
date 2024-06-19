package com.fonctionpublique.controllers;

import com.fonctionpublique.services.certification.CertificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/certification")
@CrossOrigin
public class CertificationController {

    private final CertificationServiceImpl certificationServiceImpl;
//    private final WebClient webClient;

    @GetMapping("/generatedAttestation/{id}")
    public String generatedAttestationNumber(@PathVariable(name = "id") int id) {
        return certificationServiceImpl.generateAttestationNumber(id);
    }

//    @GetMapping("/timeOut/{id}")
//    public String getWithWebClient(@PathVariable int id) {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("localhost:8080/api/**")
//                        .queryParam("id", id)
//                        .build())
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }


}
