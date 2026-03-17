package com.libraryhub.ss.service;

import com.libraryhub.ss.entity.*;
import com.libraryhub.ss.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // FR-02: Submit borrow request
    public String requestBorrow(Long studentId, Long bookId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            return "No available copies for: " + book.getTitle();
        }

        boolean alreadyActive = borrowRecordRepository
                .existsByStudentIdAndBookIdAndStatusIn(
                        studentId, bookId,
                        List.of(BorrowStatus.REQUESTED, BorrowStatus.APPROVED));

        if (alreadyActive) {
            return "You already have an active request for: " + book.getTitle();
        }

        BorrowRecord record = new BorrowRecord();
        record.setStudent(student);
        record.setBook(book);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        record.setStatus(BorrowStatus.REQUESTED);
        borrowRecordRepository.save(record);

        return "Borrow request submitted for: " + book.getTitle();
    }

    // FR-04: Get student borrow history
    public List<BorrowRecord> getStudentHistory(Long studentId) {
        return borrowRecordRepository.findByStudentId(studentId);
    }
}