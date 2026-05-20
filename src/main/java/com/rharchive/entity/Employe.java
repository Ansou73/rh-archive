// entity/Employe.java
package com.rharchive.entity;

import com.rharchive.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "employes")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Employe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 3)
    private String initiales;

    @Column(nullable = false, length = 150)
    private String poste;

    @Column(nullable = false, length = 100)
    private String departement;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeContrat contrat;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEmploye statut = StatutEmploye.Actif;

    @Builder.Default
    @Column(length = 10)
    private String couleur = "#7c3aed";

    @Column(name = "date_embauche", nullable = false)
    private LocalDate dateEmbauche;

    @Column(name = "date_archivage")
    private LocalDateTime dateArchivage;

    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Génère les initiales depuis le nom
    @PrePersist
    void prePersist() {
        if (this.initiales == null || this.initiales.isBlank()) {
            String[] parts = this.nom.trim().split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String p : parts) if (!p.isEmpty()) sb.append(p.charAt(0));
            this.initiales = sb.toString().toUpperCase().substring(0, Math.min(2, sb.length()));
        }
    }
}
