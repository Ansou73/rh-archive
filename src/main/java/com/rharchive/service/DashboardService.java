// service/DashboardService.java
package com.rharchive.service;

import com.rharchive.dto.response.DashboardStatsResponse;
import com.rharchive.enums.StatutEmploye;
import com.rharchive.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service @RequiredArgsConstructor
public class DashboardService {
    private final EmployeRepository empRepo;
    private final DocumentRepository docRepo;
    private final AlerteRepository alerteRepo;

    public DashboardStatsResponse getStats() {
        Map<String, Long> contrats = new LinkedHashMap<>();
        empRepo.countByContrat().forEach(row -> contrats.put((String) row[0], (Long) row[1]));

        return DashboardStatsResponse.builder()
            .totalEmployesActifs(empRepo.countByStatut(StatutEmploye.Actif))
            .totalArchives(empRepo.countByStatut(StatutEmploye.Archivé))
            .totalDocuments(docRepo.count())
            .totalAlertes(alerteRepo.count())
            .alertesNonLues(alerteRepo.countByLueFalse())
            .repartitionContrats(contrats)
            .build();
    }
}