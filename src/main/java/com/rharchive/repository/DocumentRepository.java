// repository/DocumentRepository.java
package com.rharchive.repository;

import com.rharchive.entity.Document;
import com.rharchive.enums.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByEmployeId(Long employeId);
    List<Document> findByEmployeIdAndTypeDocument(Long employeId, TypeDocument type);
    long countByEmployeId(Long employeId);
}