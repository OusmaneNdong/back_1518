package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.entities.FileUploadResponse;
import com.fonctionpublique.services.uploadFile.FileUploadServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
public class FileUploadController {

    private final FileUploadServiceImpl fileUploadServiceImpl;

    @Autowired
    public FileUploadController(FileUploadServiceImpl fileUploadServiceImpl) {
        this.fileUploadServiceImpl = fileUploadServiceImpl;
    }

    @PostMapping
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("ownedBy") String ownedBy,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) throws IOException {
        FileUpload theFile = fileUploadServiceImpl.uploadFile(ownedBy, description, file);
        String downloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
//                .path("/api/uploads/download/")
                .path("/Users/7maksacodpc/Desktop/Attestation/Attestation/uploaded/documents")
                .path(String.valueOf(theFile.getId()))
                .toUriString();
        FileUploadResponse response = new FileUploadResponse(
                theFile.getName(),
                downloadUri,
                file.getContentType(),
                file.getSize()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//    public ResponseEntity<FileUpload> uploadFile(
//            @RequestParam("ownedBy")  String ownedBy,
//            @RequestParam("description") String description,
//            @RequestParam("file")MultipartFile file) throws IOException {
//        FileUpload theFile = fileUploadServiceImpl.uploadFile(ownedBy,description,file);
//        FileUpload dowload = ServletUriComponentsBuilder
//                .fromCurrentContextPath()
//                .path("/download/")
//                .path(String.valueOf(theFile.getId()))
//                .toUriString();
//        return new ResponseEntity<>(dowload , HttpStatus.OK);
////        return new FileUpload(theFile.getName(),dowload, file.getContentType(),file.getSize());
//    }


    @GetMapping("/downoad/{file}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") long fileId) throws Exception {
        FileUpload fileUpload = null;
        fileUpload = fileUploadServiceImpl.download(fileId);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileUpload.getType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "fileupload; fileName\"" + fileUpload.getName()+"\"")
                .body(new ByteArrayResource(fileUpload.getFile()));

    }
}
