package com.fonctionpublique.services.uploadFile;

import com.fonctionpublique.entities.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    FileUpload uploadFile(String ownedBy, String description, MultipartFile file) throws IOException;

    FileUpload download(long fileId) throws Exception;
}
