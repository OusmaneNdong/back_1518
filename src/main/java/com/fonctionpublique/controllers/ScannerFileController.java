package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.ResponseMessage;
import com.fonctionpublique.entities.ScannerFile;
import com.fonctionpublique.entities.ScannerFileResponse;
import com.fonctionpublique.services.scannerFile.ScannerFileServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scanner")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name="scannerFile")
public class ScannerFileController {
    
}
