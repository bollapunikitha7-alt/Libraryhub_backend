package com.libraryhub_borrow_service.controller;

import com.libraryhub_borrow_service.entity.BorrowRecord;
import com.libraryhub_borrow_service.service.BorrowService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8081") 
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

   

    @PostMapping("/borrow-requests")
    public ResponseEntity<BorrowRecord> requestBorrow(@RequestBody Map<String, String> body) {
        Long studentId  = Long.parseLong(body.get("studentId"));
        Long bookId     = Long.parseLong(body.get("bookId"));
        LocalDate dueDate = LocalDate.parse(body.get("dueDate"));

        BorrowRecord created = borrowService.requestBorrow(studentId, bookId, dueDate);
        return ResponseEntity.ok(created);
    }


    @GetMapping("/librarian/borrow-requests")
    public ResponseEntity<List<BorrowRecord>> getAllRequested() {
        return ResponseEntity.ok(borrowService.getAllRequestedBorrows());
    }

    

    @PutMapping("/librarian/borrow-request/{id}")
    public ResponseEntity<?> updateBorrowStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return switch (status.toUpperCase()) {
            case "APPROVED" -> ResponseEntity.ok(borrowService.approveBorrow(id));
            case "REJECTED" -> ResponseEntity.ok(borrowService.rejectBorrow(id));
            default         -> ResponseEntity.badRequest()
                                   .body("Invalid status. Use APPROVED or REJECTED.");
        };
    }

   

    @GetMapping("/borrow-requests/student/{studentId}")
    public ResponseEntity<List<BorrowRecord>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(borrowService.getRecordsByStudent(studentId));
    }

  

    @GetMapping("/borrow-requests/book/{bookId}")
    public ResponseEntity<List<BorrowRecord>> getByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowService.getRecordsByBook(bookId));
    }
}