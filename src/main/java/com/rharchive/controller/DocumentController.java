// controller/DocumentController.java
package com.rharchive.controller;

import com.rharchive.entity.Document;
import com.rharchive.enums.TypeDocument;
import com.rharchive.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController @RequiredArgsConstructor
public class DocumentController {
    private final DocumentService service;

    @GetMapping("/api/v1/employes/{id}/documents")
    public ResponseEntity<List<Document>> lister(@PathVariable Long id,
        @RequestParam(required = false) TypeDocument type) {
        return ResponseEntity.ok(service.lister(id, type));
    }

    @PostMapping("/api/v1/employes/{id}/documents")
    public ResponseEntity<Document> ajouter(
        @PathVariable Long id,
        @RequestParam String nom,
        @RequestParam TypeDocument type,
        @RequestParam(required = false) LocalDate dateDocument,
        @RequestParam MultipartFile fichier,
        Authentication auth) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.ajouter(id, nom, type, dateDocument, fichier, auth.getName()));
    }

    @GetMapping("/api/v1/employes/{id}/documents/{docId}")
    public ResponseEntity<Document> detail(@PathVariable Long id, @PathVariable Long docId) {
        return ResponseEntity.ok(service.findById(docId));
    }

    @GetMapping("/api/v1/employes/{id}/documents/{docId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id, @PathVariable Long docId) throws IOException {
        Resource res = service.telecharger(id, docId);
        Document doc = service.findById(docId);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(
                doc.getMimeType() != null ? doc.getMimeType() : "application/octet-stream"))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + doc.getNomFichier() + "\"")
            .body(res);
    }

    @PostMapping("/api/v1/numerisation/upload")
    public ResponseEntity<Document> numeriser(
        @RequestParam Long employeId,
        @RequestParam String nom,
        @RequestParam TypeDocument type,
        @RequestParam MultipartFile fichier,
        Authentication auth) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.ajouter(employeId, nom, type, null, fichier, auth.getName()));
    }

    @DeleteMapping("/api/v1/employes/{id}/documents/{docId}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id, @PathVariable Long docId) throws IOException {
        service.supprimer(id, docId);
        return ResponseEntity.noContent().build();
    }
}