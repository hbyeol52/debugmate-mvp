package com.debugmate.errorcase;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ErrorCaseRepository extends JpaRepository<ErrorCase, Long> {
    List<ErrorCase> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);
    List<ErrorCase> findAllByOrderByCreatedAtDesc();
    long countByStatus(ErrorStatus status);
}
