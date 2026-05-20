// repository/AlerteRepository.java
package com.rharchive.repository;

import com.rharchive.entity.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    List<Alerte> findByLue(boolean lue);
    long countByLueFalse();
}