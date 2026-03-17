package com.libraryhub_borrow_service.service;

import com.libraryhub_borrow_service.entity.BorrowRecord;
import com.libraryhub_borrow_service.entity.BorrowRecord.Status;
import com.libraryhub_borrow_service.repository.BorrowRecordRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowService(BorrowRecordRepository borrowRecordRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
    }

 
    
    public BorrowRecord requestBorrow(Long studentId, Long bookId, LocalDate dueDate) {
        BorrowRecord record = new BorrowRecord(studentId, bookId, LocalDate.now(), dueDate);
        return borrowRecordRepository.save(record);
    }

   
    public BorrowRecord approveBorrow(Long recordId) {
        BorrowRecord record = findOrThrow(recordId);

        if (record.getStatus() != Status.REQUESTED) {
            throw new IllegalStateException(
                "Cannot approve a record that is already " + record.getStatus());
        }

        record.setStatus(Status.APPROVED);
        return borrowRecordRepository.save(record);
    }


    public BorrowRecord rejectBorrow(Long recordId) {
        BorrowRecord record = findOrThrow(recordId);

        if (record.getStatus() != Status.REQUESTED) {
            throw new IllegalStateException(
                "Cannot reject a record that is already " + record.getStatus());
        }

        record.setStatus(Status.REJECTED);
        return borrowRecordRepository.save(record);
    }


   
    @Transactional(readOnly = true)
    public List<BorrowRecord> getAllRequestedBorrows() {
        return borrowRecordRepository.findByStatus(Status.REQUESTED);
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> getRecordsByStudent(Long studentId) {
        return borrowRecordRepository.findByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public List<BorrowRecord> getRecordsByBook(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId);
    }


    private BorrowRecord findOrThrow(Long id) {
        return borrowRecordRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("BorrowRecord not found with id: " + id));
    }
}