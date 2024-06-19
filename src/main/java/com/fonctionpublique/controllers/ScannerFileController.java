package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.ResponseMessage;
import com.fonctionpublique.entities.ScannerFile;
import com.fonctionpublique.entities.ScannerFileResponse;
import com.fonctionpublique.services.scannerFile.ScannerFileServiceImpl;
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
public class ScannerFileController {

//    private final ScannerFileServiceImpl scannerFileServiceImpl;
//
//
////    public ScannerFileResponse uploadFichier(@RequestParam("fichier") MultipartFile fichier) throws Exception {
////        ScannerFile scannerFile = null;
////        scannerFile = scannerFileServiceImpl.saveScannerFile(fichier);
////        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
////                .path("/Downloads/")
////                .path(scannerFile.getId())
////                .toUriString();
////        return new ScannerFileResponse(scannerFile.getName(),
////                downloadURL,
////                fichier.getContentType(),
////                (int) fichier.getSize());
////    }
//    @PostMapping("/upload")
//    public ScannerFileResponse uploadFichier(@RequestParam("fichier") MultipartFile fichier) throws Exception {
//        ScannerFile scannerFile = null;
//        String downloadURL = "";
//        scannerFile = scannerFileServiceImpl.saveScannerFile(fichier);
//        downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
////                .path("/api/scanner/telechargement/")
//                .path("/Users/7maksacodpc/Downloads/")
//                .path(scannerFile.getId())
//                .toUriString();
//        return  new ScannerFileResponse(scannerFile.getName(),
//                downloadURL,
//                fichier.getContentType(),
//                (int) fichier.getSize());
//
////    }
////    @GetMapping("/telechargement/{id}")
////    public ResponseEntity<Resource> telechargerFichier(@PathVariable String fichierId) throws Exception {
////        ScannerFile scannerFile = scannerFileServiceImpl.getScannerFile(fichierId);
////        URI uploadDir = null;
////        Path filePath = Paths.get(uploadDir).resolve(scannerFile.getNomAttestation()).normalize();
////        Resource resource = new UrlResource(filePath.toUri());
////        return ResponseEntity.ok()
////                .contentType(MediaType.parseMediaType(scannerFile.getTypeFichier()))
////                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + scannerFile.getNomAttestation() + "\"")
////                .body(resource);
////    }
////    public ResponseEntity<Resource> telechargerFichier(@PathVariable String fichierId) throws Exception {
////        ScannerFile scannerFile = null;
////        scannerFile = scannerFileServiceImpl.getScannerFile(fichierId);
////        return  ResponseEntity.ok()
////                .contentType(MediaType.parseMediaType(scannerFile.getTypeFichier()))
////                .header(HttpHeaders.CONTENT_DISPOSITION,
////                        "scannerFile; nomAttestation\"" + scannerFile.getNomAttestation()
////                                +"\"")
////                .body(new ByteArrayResource(scannerFile.getData()));
////    }
////        @PostMapping("/upload")
////        public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
////            String message = "";
////            try {
////                scannerFileServiceImpl.store(file);
////
////                message = "Uploaded the file successfully: " + file.getOriginalFilename();
////                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
////            } catch (Exception e) {
////                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
////                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
////            }
////        }
////
////        @GetMapping("/files")
////        public ResponseEntity<List<ScannerFileResponse>> getListFiles() {
////            List<ScannerFileResponse> files = scannerFileServiceImpl.getAllFiles().map(dbFile -> {
////                String fileDownloadUri = ServletUriComponentsBuilder
////                        .fromCurrentContextPath()
////                        .path("/Downoads/")
////                        .path(dbFile.getId())
////                        .toUriString();
////
////                return new ScannerFileResponse(
////                        dbFile.getName(),
////                        fileDownloadUri,
////                        dbFile.getType(),
////                        dbFile.getData().length);
////            }).collect(Collectors.toList());
////
////            return ResponseEntity.status(HttpStatus.OK).body(files);
//        }
//
//        @GetMapping("/telechargement/{id}")
//        public ResponseEntity<byte[]> getFile(@PathVariable String id) {
//            ScannerFile fileDB = scannerFileServiceImpl.getFile(id);
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
//                    .body(fileDB.getData());
//        }

    private final ScannerFileServiceImpl scannerFileServiceImpl;

    @PostMapping("/upload")
    public ResponseEntity<ScannerFileResponse> uploadFichier(@RequestParam("fichier") MultipartFile fichier) {
        try {
            ScannerFile scannerFile = scannerFileServiceImpl.saveScannerFile(fichier);
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/scanner/telechargement/")
                    .path(scannerFile.getId())
                    .toUriString();
            ScannerFileResponse response = new ScannerFileResponse(scannerFile.getName(), downloadURL, fichier.getContentType(), (int) fichier.getSize());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ScannerFileResponse("Failed to upload file", null, null, 0));
        }
    }

    @GetMapping("/telechargement/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id) {
        try {
            ScannerFile fileDB = scannerFileServiceImpl.getFile(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileDB.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(new ByteArrayResource(fileDB.getData()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ScannerFileResponse>> getListFiles() {
        List<ScannerFileResponse> files = scannerFileServiceImpl.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/scanner/telechargement/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ScannerFileResponse(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

}
