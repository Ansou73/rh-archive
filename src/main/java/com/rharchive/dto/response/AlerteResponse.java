// dto/response/AlerteResponse.java
// ════════════════════════════════════════════════════════════
package com.rharchive.dto.response;

import com.rharchive.enums.TypeAlerte;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlerteResponse {

    private Long         id;
    private String       message;
    private TypeAlerte   typeAlerte;
    private boolean      lue;
    private LocalDateTime createdAt;

    // Infos réduites de l'employé concerné (peut être null)
    private Long   employeId;
    private String employeNom;
    private String employeInitiales;
    private String employeCouleur;

    /** Fabrique statique depuis l'entité */
    public static AlerteResponse from(com.rharchive.entity.Alerte a) {
        AlerteResponseBuilder b = AlerteResponse.builder()
            .id(a.getId())
            .message(a.getMessage())
            .typeAlerte(a.getTypeAlerte())
            .lue(a.isLue())
            .createdAt(a.getCreatedAt());

        if (a.getEmploye() != null) {
            b.employeId(a.getEmploye().getId())
             .employeNom(a.getEmploye().getNom())
             .employeInitiales(a.getEmploye().getInitiales())
             .employeCouleur(a.getEmploye().getCouleur());
        }
        return b.build();
    }
}