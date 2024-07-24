package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.entities.FileUploadResponse;
import com.fonctionpublique.entities.Params;
import com.fonctionpublique.services.uploadFile.FileUploadServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api/uploads")
@Tag(name="fileupload")
public class FileUploadController {

    private final FileUploadServiceImpl fileUploadServiceImpl;

    @Autowired
    public FileUploadController(FileUploadServiceImpl fileUploadServiceImpl) {
        this.fileUploadServiceImpl = fileUploadServiceImpl;
    }

    @PostMapping("/{id}")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file, @PathVariable("id") int id) throws IOException {
        FileUploadResponse theFile = fileUploadServiceImpl.uploadFile(file,id);

        return new ResponseEntity<>(theFile, HttpStatus.OK);
    }


    @GetMapping("/downoad/{file}")
    public ResponseEntity<Resource> download(@PathVariable("file") long fileId) throws Exception {
        FileUpload fileUpload = null;
        fileUpload = fileUploadServiceImpl.download(fileId);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileUpload.getType()))
                .header(CONTENT_DISPOSITION, "fileupload; fileName\"" + fileUpload.getName()+"\"")
                .body(new ByteArrayResource(fileUpload.getFile()));

    }



    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Path filePath = get(Params.DIRECTORYATTESTATION).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }

}