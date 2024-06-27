package com.fonctionpublique.services.scannerFile;

import com.fonctionpublique.entities.ScannerFile;
import com.fonctionpublique.entities.ScannerFileResponse;
import com.fonctionpublique.repository.ScannerFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ScannerFileServiceImpl implements ScannerFileService{

//    private final ScannerFileRepository scannerFileRepository;
//
//    @Override
//    public ScannerFile store(MultipartFile file) throws IOException {
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//        ScannerFile FileDB = new ScannerFile(fileName, file.getContentType(), file.getBytes());
//
//        return scannerFileRepository.save(FileDB);
//    }
//    @Override
//    public ScannerFile getFile(String id) {
//
//        return scannerFileRepository.findById(id).get();
//    }
//
//    @Override
//    public Stream<ScannerFile> getAllFiles() {
//
//        return scannerFileRepository.findAll().stream();
//    }
//
//    public ScannerFile saveScannerFile(MultipartFile fichier) throws Exception {
//        String nomFichier = StringUtils.cleanPath(Objects.requireNonNull(fichier.getOriginalFilename()));
//        try{
//            if(nomFichier.contains("..")){
//                throw new Exception("le nom du fichier est incorrect" + nomFichier);
//            }
//
//            ScannerFile scannerFile = new ScannerFile(nomFichier,
//                    fichier.getContentType(),
//                    fichier.getBytes());
//            return scannerFileRepository.save(scannerFile);
//
//        } catch (Exception e){
//            throw  new Exception("impossible d'enregistrer le fichier" + nomFichier);
//
//        }
//    }


//    private final ScannerFileRepository scannerFileRepository;
//
//    @Override
//    public ScannerFile store(MultipartFile file) throws IOException {
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
//        ScannerFile FileDB = new ScannerFile(fileName, file.getContentType(), file.getBytes());
//
//        return scannerFileRepository.save(FileDB);
//    }
//
//    @Override
//    public ScannerFile getFile(String id) {
//        return scannerFileRepository.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
//    }
//
//    @Override
//    public Stream<ScannerFile> getAllFiles() {
//        return scannerFileRepository.findAll().stream();
//    }
//
//    public ScannerFile saveScannerFile(MultipartFile fichier) throws Exception {
//        String nomFichier = StringUtils.cleanPath(Objects.requireNonNull(fichier.getOriginalFilename()));
//        if (nomFichier.contains("..")) {
//            throw new Exception("Le nom du fichier est incorrect: " + nomFichier);
//        }
//        ScannerFile scannerFile = new ScannerFile(nomFichier, fichier.getContentType(), fichier.getBytes());
//        return scannerFileRepository.save(scannerFile);
//    }
}
