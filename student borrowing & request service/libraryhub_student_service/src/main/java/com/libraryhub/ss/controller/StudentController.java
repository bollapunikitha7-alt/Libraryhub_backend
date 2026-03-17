package com.libraryhub.ss.controller;

import com.libraryhub.ss.entity.Book;
import com.libraryhub.ss.entity.BorrowRecord;
import com.libraryhub.ss.service.BookService;
import com.libraryhub.ss.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins="https://localhost:8081/")
@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowService borrowService;

    // FR-02: GET /api/student/books
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {

        List<Book> books;
        if (title != null && !title.isBlank()) {
            books = bookService.searchByTitle(title);
        } else if (author != null && !author.isBlank()) {
            books = bookService.searchByAuthor(author);
        } else {
            books = bookService.getAllBooks();
        }
        return ResponseEntity.ok(books);
    }

    // FR-02: POST /api/student/borrow?bookId=&studentId=
    @PostMapping("/borrow")
    public ResponseEntity<String> borrowBook(
            @RequestParam Long bookId,
            @RequestParam Long studentId) {
        return ResponseEntity.ok(borrowService.requestBorrow(studentId, bookId));
    }

    // FR-04: GET /api/student/history?studentId=
    @GetMapping("/history")
    public ResponseEntity<List<BorrowRecord>> getHistory(
            @RequestParam Long studentId) {
        return ResponseEntity.ok(borrowService.getStudentHistory(studentId));
    }
}