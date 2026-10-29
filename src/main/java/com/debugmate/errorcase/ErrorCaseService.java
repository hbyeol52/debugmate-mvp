package com.debugmate.errorcase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ErrorCaseService {
    private final ErrorCaseRepository repository;
    public ErrorCaseService(ErrorCaseRepository repository) { this.repository = repository; }

    public List<ErrorCase> findAll(String query) {
        if (query == null || query.isBlank()) return repository.findAllByOrderByCreatedAtDesc();
        return repository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(query);
    }
    public ErrorCase find(Long id) { return repository.findById(id).orElseThrow(); }
    @Transactional public ErrorCase save(ErrorCase errorCase) { return repository.save(errorCase); }
    @Transactional public void updateStatus(Long id, ErrorStatus status) { find(id).setStatus(status); }
    @Transactional public void delete(Long id) { repository.delete(find(id)); }
    public long countAll() { return repository.count(); }
    public long countResolved() { return repository.countByStatus(ErrorStatus.RESOLVED); }
    public long countReviewRequired() { return repository.countByStatus(ErrorStatus.REVIEW_REQUIRED); }
}
