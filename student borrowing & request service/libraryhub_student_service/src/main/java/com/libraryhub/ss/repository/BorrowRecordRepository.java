package com.libraryhub.ss.repository;

import com.libraryhub.ss.entity.BorrowRecord;
import com.libraryhub.ss.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByStudentId(Long studentId);
    boolean existsByStudentIdAndBookIdAndStatusIn(
        Long studentId, Long bookId, List<BorrowStatus> statuses);
}