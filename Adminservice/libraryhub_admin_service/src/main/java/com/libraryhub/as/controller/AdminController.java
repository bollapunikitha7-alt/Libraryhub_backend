package com.libraryhub.as.controller;

import com.libraryhub.as.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:8081"})
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ── Users ──────────────────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        if (!adminService.deleteUser(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable Long id) {
        if (!adminService.deactivateUser(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "User deactivated"));
    }

    @PutMapping("/users/{id}/activate")
    public ResponseEntity<Map<String, String>> activateUser(@PathVariable Long id) {
        if (!adminService.activateUser(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "User activated"));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role) {
        if (!adminService.updateUserRole(id, role)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("message", "Role updated to " + role));
    }

    // ── Borrow Records ─────────────────────────────────────────────────────

    @GetMapping("/borrows")
    public ResponseEntity<List<Map<String, Object>>> getAllBorrowRecords() {
        return ResponseEntity.ok(adminService.getAllBorrowRecords());
    }

    @GetMapping("/borrows/user/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getBorrowsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getBorrowsByUser(userId));
    }

    // ── Reports ────────────────────────────────────────────────────────────

    @GetMapping("/reports")
    public ResponseEntity<List<Map<String, Object>>> getReports() {
        return ResponseEntity.ok(adminService.getReports());
    }
}