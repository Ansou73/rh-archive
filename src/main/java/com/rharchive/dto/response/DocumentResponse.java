// dto/response/DocumentResponse.java
// ════════════════════════════════════════════════════════════
package com.rharchive.dto.response;

import com.rharchive.enums.TypeDocument;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentResponse {

    private Long          id;
    private String        nom;
    private TypeDocument  typeDocument;
    private String        nomFichier;
    private Long          tailleFichier;       // en octets
    private String        tailleFormatee;      // ex : "1.2 Mo"
    private String        mimeType;
    private LocalDate     dateDocument;
    private String        urlTelechargement;   // /api/v1/employes/{id}/documents/{docId}/download
    private String        ajoutePar;
    private LocalDateTime createdAt;

    // Infos réduites de l'employé propriétaire
    private Long   employeId;
    private String employeNom;

    /** Fabrique statique depuis l'entité */
    public static DocumentResponse from(com.rharchive.entity.Document d) {
        String url = String.format("/api/v1/employes/%d/documents/%d/download",
                d.getEmploye().getId(), d.getId());

        return DocumentResponse.builder()
            .id(d.getId())
            .nom(d.getNom())
            .typeDocument(d.getTypeDocument())
            .nomFichier(d.getNomFichier())
            .tailleFichier(d.getTailleFichier())
            .tailleFormatee(formaterTaille(d.getTailleFichier()))
            .mimeType(d.getMimeType())
            .dateDocument(d.getDateDocument())
            .urlTelechargement(url)
            .ajoutePar(d.getAjoutePar())
            .createdAt(d.getCreatedAt())
            .employeId(d.getEmploye().getId())
            .employeNom(d.getEmploye().getNom())
            .build();
    }

    /** Convertit les octets en Ko / Mo / Go lisibles */
    private static String formaterTaille(Long octets) {
        if (octets == null || octets == 0) return "0 o";
        if (octets < 1024)                 return octets + " o";
        if (octets < 1024 * 1024)          return String.format("%.1f Ko", octets / 1024.0);
        if (octets < 1024 * 1024 * 1024)   return String.format("%.1f Mo", octets / (1024.0 * 1024));
        return String.format("%.1f Go", octets / (1024.0 * 1024 * 1024));
    }
}