package com.libraryhub_borrow_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    public enum Status {
        REQUESTED, APPROVED, REJECTED, RETURNED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private Long studentId;

    // Book ID (references Book service)
    @Column(nullable = false)
    private Long bookId;

    @Column(nullable = false)
    private LocalDate borrowDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.REQUESTED;



    public BorrowRecord() {}

    public BorrowRecord(Long studentId, Long bookId, LocalDate borrowDate, LocalDate dueDate) {
        this.studentId = studentId;
        this.bookId    = bookId;
        this.borrowDate = borrowDate;
        this.dueDate    = dueDate;
        this.status     = Status.REQUESTED;
    }



    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public Long getStudentId()             { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getBookId()                { return bookId; }
    public void setBookId(Long bookId)     { this.bookId = bookId; }

    public LocalDate getBorrowDate()       { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate()          { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate()       { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Status getStatus()              { return status; }
    public void setStatus(Status status)   { this.status = status; }
}