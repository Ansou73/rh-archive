// dto/response/DashboardStatsResponse.java
package com.rharchive.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data @Builder
public class DashboardStatsResponse {
    private long totalEmployesActifs;
    private long totalArchives;
    private long totalDocuments;
    private long totalAlertes;
    private long alertesNonLues;
    private Map<String, Long> repartitionContrats;
    private Map<String, Long> repartitionDepartements;
}