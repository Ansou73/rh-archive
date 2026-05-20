// repository/EmployeRepository.java
package com.rharchive.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rharchive.entity.Employe;
import com.rharchive.enums.StatutEmploye;

public interface EmployeRepository extends JpaRepository<Employe, Long> {

    Page<Employe> findByStatut(StatutEmploye statut, Pageable pageable);

    @Query("""
        SELECT e FROM Employe e
        WHERE e.statut = :statut
          AND (:search IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%',:search,'%'))
               OR LOWER(e.poste) LIKE LOWER(CONCAT('%',:search,'%')))
          AND (:dept IS NULL OR e.departement = :dept)
    """)
    Page<Employe> search(@Param("statut") StatutEmploye statut,
                         @Param("search") String search,
                         @Param("dept") String departement,
                         Pageable pageable);

    long countByStatut(StatutEmploye statut);

    @Query("SELECT e.contrat, COUNT(e) FROM Employe e GROUP BY e.contrat")
    java.util.List<Object[]> countByContrat();
}