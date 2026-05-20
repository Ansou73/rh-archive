// service/DocumentService.java
package com.rharchive.service;

import com.rharchive.entity.*;
import com.rharchive.enums.TypeDocument;
import com.rharchive.exception.ResourceNotFoundException;
import com.rharchive.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional
public class DocumentService {
    private final DocumentRepository docRepo;
    private final EmployeRepository empRepo;

    @Value("${app.upload.dir}") private String uploadDir;

    public List<Document> lister(Long empId, TypeDocument type) {
        return type != null
            ? docRepo.findByEmployeIdAndTypeDocument(empId, type)
            : docRepo.findByEmployeId(empId);
    }

    public Document ajouter(Long empId, String nom, TypeDocument type,
                            LocalDate dateDoc, MultipartFile fichier, String ajoutePar) throws IOException {
        Employe emp = empRepo.findById(empId)
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

        Path dir = Paths.get(uploadDir, String.valueOf(empId));
        Files.createDirectories(dir);

        String ext = getExtension(fichier.getOriginalFilename());
        String nomFichier = UUID.randomUUID() + ext;
        Path dest = dir.resolve(nomFichier);
        fichier.transferTo(dest);

        Document doc = Document.builder()
            .nom(nom).typeDocument(type).dateDocument(dateDoc)
            .nomFichier(fichier.getOriginalFilename())
            .cheminFichier(dest.toString())
            .tailleFichier(fichier.getSize())
            .mimeType(fichier.getContentType())
            .employe(emp).ajoutePar(ajoutePar)
            .build();
        return docRepo.save(doc);
    }

    public Resource telecharger(Long empId, Long docId) throws IOException {
        Document doc = docRepo.findById(docId)
            .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé"));
        if (!doc.getEmploye().getId().equals(empId))
            throw new ResourceNotFoundException("Document non associé à cet employé");
        Path path = Paths.get(doc.getCheminFichier());
        Resource res = new UrlResource(path.toUri());
        if (!res.exists()) throw new ResourceNotFoundException("Fichier introuvable sur le disque");
        return res;
    }

    public Document findById(Long docId) {
        return docRepo.findById(docId)
            .orElseThrow(() -> new ResourceNotFoundException("Document non trouvé : " + docId));
    }

    public void supprimer(Long empId, Long docId) throws IOException {
        Document doc = findById(docId);
        Files.deleteIfExists(Paths.get(doc.getCheminFichier()));
        docRepo.deleteById(docId);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}