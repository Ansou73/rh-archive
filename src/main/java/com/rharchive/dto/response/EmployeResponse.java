// dto/response/EmployeResponse.java
// ════════════════════════════════════════════════════════════
package com.rharchive.dto.response;

import com.rharchive.enums.StatutEmploye;
import com.rharchive.enums.TypeContrat;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeResponse {

    private Long          id;
    private String        nom;
    private String        initiales;
    private String        poste;
    private String        departement;
    private String        email;
    private String        telephone;
    private TypeContrat   contrat;
    private StatutEmploye statut;
    private String        couleur;
    private LocalDate     dateEmbauche;
    private LocalDateTime dateArchivage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int                    nombreDocuments;
    private List<DocumentResponse> documents;

    /** Fabrique statique (sans documents — pour les listes) */
    public static EmployeResponse fromLight(com.rharchive.entity.Employe e) {
        return EmployeResponse.builder()
            .id(e.getId())
            .nom(e.getNom())
            .initiales(e.getInitiales())
            .poste(e.getPoste())
            .departement(e.getDepartement())
            .email(e.getEmail())
            .telephone(e.getTelephone())
            .contrat(e.getContrat())
            .statut(e.getStatut())
            .couleur(e.getCouleur())
            .dateEmbauche(e.getDateEmbauche())
            .dateArchivage(e.getDateArchivage())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .nombreDocuments(e.getDocuments() != null ? e.getDocuments().size() : 0)
            .build();
    }

    /** Fabrique statique complète (avec documents — pour la fiche détail) */
    public static EmployeResponse fromFull(com.rharchive.entity.Employe e) {
        EmployeResponse r = fromLight(e);
        if (e.getDocuments() != null) {
            r.setDocuments(
                e.getDocuments().stream()
                 .map(DocumentResponse::from)
                 .collect(Collectors.toList())
            );
        }
        return r;
    }
}