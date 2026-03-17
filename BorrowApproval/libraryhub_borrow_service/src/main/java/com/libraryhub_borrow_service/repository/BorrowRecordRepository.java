package com.libraryhub_borrow_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libraryhub_borrow_service.entity.BorrowRecord;
import com.libraryhub_borrow_service.entity.BorrowRecord.Status;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    
    List<BorrowRecord> findByStudentId(Long studentId);

   
    List<BorrowRecord> findByStatus(Status status);

  
    List<BorrowRecord> findByBookId(Long bookId);
}
