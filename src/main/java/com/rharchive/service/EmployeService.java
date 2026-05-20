// service/EmployeService.java
package com.rharchive.service;

import com.rharchive.dto.request.EmployeRequest;
import com.rharchive.entity.Employe;
import com.rharchive.enums.StatutEmploye;
import com.rharchive.exception.ResourceNotFoundException;
import com.rharchive.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service @RequiredArgsConstructor @Transactional
public class EmployeService {
    private final EmployeRepository repo;

    public Page<Employe> lister(StatutEmploye statut, String search, String dept, Pageable pageable) {
        if (statut == null) statut = StatutEmploye.Actif;
        return repo.search(statut, search, dept, pageable);
    }

    public Employe findById(Long id) {
        return repo.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("Employé non trouvé : " + id));
    }

    public Employe creer(EmployeRequest req) {
        Employe e = Employe.builder()
            .nom(req.getNom()).poste(req.getPoste())
            .departement(req.getDepartement()).email(req.getEmail())
            .telephone(req.getTelephone()).contrat(req.getContrat())
            .dateEmbauche(req.getDateEmbauche())
            .couleur(req.getCouleur() != null ? req.getCouleur() : "#7c3aed")
            .statut(StatutEmploye.Actif).build();
        return repo.save(e);
    }

    public Employe modifier(Long id, EmployeRequest req) {
        Employe e = findById(id);
        e.setNom(req.getNom()); e.setPoste(req.getPoste());
        e.setDepartement(req.getDepartement()); e.setEmail(req.getEmail());
        e.setTelephone(req.getTelephone()); e.setContrat(req.getContrat());
        e.setDateEmbauche(req.getDateEmbauche());
        return repo.save(e);
    }

    public Employe archiver(Long id) {
        Employe e = findById(id);
        e.setStatut(StatutEmploye.Archivé);
        e.setDateArchivage(LocalDateTime.now());
        return repo.save(e);
    }

    public Employe restaurer(Long id) {
        Employe e = findById(id);
        e.setStatut(StatutEmploye.Actif);
        e.setDateArchivage(null);
        return repo.save(e);
    }

    public void supprimer(Long id) {
        findById(id);
        repo.deleteById(id);
    }
}