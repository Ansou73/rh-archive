// controller/AlerteController.java
package com.rharchive.controller;

import com.rharchive.entity.Alerte;
import com.rharchive.repository.AlerteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/alertes")
@RequiredArgsConstructor
public class AlerteController {
    private final AlerteRepository repo;

    @GetMapping
    public ResponseEntity<List<Alerte>> lister(
        @RequestParam(required = false) Boolean lue) {
        return ResponseEntity.ok(lue != null ? repo.findByLue(lue) : repo.findAll());
    }

    @PutMapping("/{id}/lire")
    public ResponseEntity<Alerte> marquerLue(@PathVariable Long id) {
        return repo.findById(id).map(a -> {
            a.setLue(true); return ResponseEntity.ok(repo.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/lire-tout")
    public ResponseEntity<Void> lireTout() {
        repo.findByLue(false).forEach(a -> { a.setLue(true); repo.save(a); });
        return ResponseEntity.ok().build();
    }
}