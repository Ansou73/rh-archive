// controller/ArchiveController.java
package com.rharchive.controller;

import com.rharchive.entity.Employe;
import com.rharchive.enums.StatutEmploye;
import com.rharchive.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/archives")
@RequiredArgsConstructor
public class ArchiveController {
    private final EmployeService service;

    @GetMapping
    public ResponseEntity<Page<Employe>> lister(
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateArchivage").descending());
        return ResponseEntity.ok(service.lister(StatutEmploye.Archivé, search, null, pageable));
    }
}