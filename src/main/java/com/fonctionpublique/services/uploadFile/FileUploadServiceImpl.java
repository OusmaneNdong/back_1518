package com.fonctionpublique.services.uploadFile;

import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.exception.FileStorageException;
import com.fonctionpublique.repository.FileUploadRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service("FileUploadServiceImpl")
public class FileUploadServiceImpl implements  FileUploadService{

    private final FileUploadRepositoy fileUploadRepositoy;
    private final Path uploadLocaton;

    @Autowired
    public FileUploadServiceImpl(FileUploadRepositoy fileUploadRepositoy, FileUpload fileUpload) {
        this.fileUploadRepositoy = fileUploadRepositoy;
        this.uploadLocaton = Paths.get(fileUpload.getUploadDir())
                .toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.uploadLocaton);
        }catch (Exception ex){
            throw  new FileStorageException("could not create directory", ex);
        }
    }

    @Override
    public FileUpload uploadFile(String ownedBy, String description, MultipartFile file) throws IOException {
        String originalFileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
        Path targetLocation = this.uploadLocaton.resolve(originalFileName);
        Files.copy(file.getInputStream(), targetLocation,
                StandardCopyOption.REPLACE_EXISTING);

        FileUpload theFile = new FileUpload();
        theFile.setDescription(description);
        theFile.setName(originalFileName);
        theFile.setOwneBy(ownedBy);
        theFile.setType(file.getContentType());
        theFile.setFile(file.getBytes());
        theFile.setUploadDir(String.valueOf(this.uploadLocaton));

        return fileUploadRepositoy.save(theFile);
    }

    @Override
    public FileUpload download(long fileId) throws Exception {
        return fileUploadRepositoy.findById(fileId)
                .orElseThrow(() -> new Exception("A file with id: " + fileId + "could not found"));
    }
}
