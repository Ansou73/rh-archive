// service/AlerteService.java
// ════════════════════════════════════════════════════════════
package com.rharchive.service;

import com.rharchive.dto.response.AlerteResponse;
import com.rharchive.entity.*;
import com.rharchive.enums.TypeAlerte;
import com.rharchive.exception.ResourceNotFoundException;
import com.rharchive.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlerteService {

    private final AlerteRepository  alerteRepo;
    private final EmployeRepository  employeRepo;

    // ── Lecture ──────────────────────────────────────────────

    /** Toutes les alertes (optionnellement filtrées par statut lu/non lu) */
    public List<AlerteResponse> lister(Boolean lue) {
        List<Alerte> alertes = (lue != null)
            ? alerteRepo.findByLue(lue)
            : alerteRepo.findAll();

        return alertes.stream()
                      .map(AlerteResponse::from)
                      .collect(Collectors.toList());
    }

    /** Nombre d'alertes non lues */
    public long compterNonLues() {
        return alerteRepo.countByLueFalse();
    }

    // ── Mutation ─────────────────────────────────────────────

    /** Marquer une alerte comme lue */
    public AlerteResponse marquerLue(Long id) {
        Alerte a = alerteRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alerte non trouvée : " + id));
        a.setLue(true);
        return AlerteResponse.from(alerteRepo.save(a));
    }

    /** Marquer toutes les alertes comme lues */
    public void lireTout() {
        List<Alerte> nonLues = alerteRepo.findByLue(false);
        nonLues.forEach(a -> a.setLue(true));
        alerteRepo.saveAll(nonLues);
    }

    /** Créer manuellement une alerte */
    public AlerteResponse creer(String message, TypeAlerte type, Long employeId) {
        Alerte.AlerteBuilder builder = Alerte.builder()
            .message(message)
            .typeAlerte(type);

        if (employeId != null) {
            employeRepo.findById(employeId).ifPresent(builder::employe);
        }
        return AlerteResponse.from(alerteRepo.save(builder.build()));
    }

    /** Supprimer une alerte */
    public void supprimer(Long id) {
        if (!alerteRepo.existsById(id))
            throw new ResourceNotFoundException("Alerte non trouvée : " + id);
        alerteRepo.deleteById(id);
    }

    // ── Génération automatique ────────────────────────────────

    /**
     * Vérifie chaque jour à 08h00 les contrats CDD / stages
     * qui expirent dans moins de 30 jours et génère une alerte.
     * Évite les doublons en vérifiant les messages existants.
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void verifierExpirationsContrats() {
        LocalDate aujourd_hui = LocalDate.now();
        LocalDate limite      = aujourd_hui.plusDays(30);

        employeRepo.findAll().stream()
            .filter(e -> e.getContrat() != null)
            .filter(e -> e.getContrat().name().equals("CDD")
                      || e.getContrat().name().equals("Stage"))
            .filter(e -> e.getDateEmbauche() != null)
            .forEach(e -> {
                // Estimation de fin de contrat (dateEmbauche + 1 an pour CDD, 6 mois pour stage)
                long mois = e.getContrat().name().equals("CDD") ? 12 : 6;
                LocalDate finContrat = e.getDateEmbauche().plusMonths(mois);

                if (!finContrat.isBefore(aujourd_hui) && !finContrat.isAfter(limite)) {
                    long joursRestants = ChronoUnit.DAYS.between(aujourd_hui, finContrat);
                    String msg = String.format(
                        "Contrat %s de %s expire dans %d jour(s) (le %s)",
                        e.getContrat().name(), e.getNom(), joursRestants, finContrat
                    );
                    // Éviter les doublons
                    boolean dejaPresente = alerteRepo.findAll().stream()
                        .anyMatch(a -> a.getMessage().contains(e.getNom())
                                    && a.getMessage().contains("expire dans"));
                    if (!dejaPresente) {
                        alerteRepo.save(Alerte.builder()
                            .message(msg)
                            .typeAlerte(TypeAlerte.warning)
                            .employe(e)
                            .build());
                    }
                }
            });
    }

    /**
     * Vérifie chaque jour à 08h30 les employés sans aucun document
     * et génère une alerte de type 'error'.
     */
    @Scheduled(cron = "0 30 8 * * *")
    public void verifierDocumentsManquants() {
        employeRepo.findAll().stream()
            .filter(e -> e.getDocuments() == null || e.getDocuments().isEmpty())
            .forEach(e -> {
                String msg = "Aucun document enregistré pour " + e.getNom();
                boolean dejaPresente = alerteRepo.findAll().stream()
                    .anyMatch(a -> a.getMessage().equals(msg) && !a.isLue());
                if (!dejaPresente) {
                    alerteRepo.save(Alerte.builder()
                        .message(msg)
                        .typeAlerte(TypeAlerte.error)
                        .employe(e)
                        .build());
                }
            });
    }
}