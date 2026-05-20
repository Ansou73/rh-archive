// controller/EmployeController.java
package com.rharchive.controller;

import com.rharchive.dto.request.EmployeRequest;
import com.rharchive.entity.Employe;
import com.rharchive.enums.StatutEmploye;
import com.rharchive.service.EmployeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/employes")
@RequiredArgsConstructor
public class EmployeController {
    private final EmployeService service;

    @GetMapping
    public ResponseEntity<Page<Employe>> lister(
        @RequestParam(required = false) StatutEmploye statut,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String departement,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "nom,asc") String sort) {
        String[] s = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(
            s.length > 1 && s[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, s[0]));
        return ResponseEntity.ok(service.lister(statut, search, departement, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employe> detail(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Employe> creer(@Valid @RequestBody EmployeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creer(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employe> modifier(@PathVariable Long id, @Valid @RequestBody EmployeRequest req) {
        return ResponseEntity.ok(service.modifier(id, req));
    }

    @PutMapping("/{id}/archiver")
    public ResponseEntity<Employe> archiver(@PathVariable Long id) {
        return ResponseEntity.ok(service.archiver(id));
    }

    @PutMapping("/{id}/restaurer")
    public ResponseEntity<Employe> restaurer(@PathVariable Long id) {
        return ResponseEntity.ok(service.restaurer(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}