package com.libraryhub.as.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final JdbcTemplate jdbc;

    public AdminService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ── Users ──────────────────────────────────────────────────────────────

    public List<Map<String, Object>> getAllUsers() {
        return jdbc.queryForList("SELECT id, username, email, role, active FROM users");
    }

    public boolean deleteUser(Long id) {
        return jdbc.update("DELETE FROM users WHERE id = ?", id) > 0;
    }

    public boolean deactivateUser(Long id) {
        return jdbc.update("UPDATE users SET active = false WHERE id = ?", id) > 0;
    }

    public boolean activateUser(Long id) {
        return jdbc.update("UPDATE users SET active = true WHERE id = ?", id) > 0;
    }

    public boolean updateUserRole(Long id, String role) {
        return jdbc.update("UPDATE users SET role = ? WHERE id = ?", role, id) > 0;
    }

    // ── Borrow Records ─────────────────────────────────────────────────────

    public List<Map<String, Object>> getAllBorrowRecords() {
        return jdbc.queryForList("SELECT * FROM borrow_records ORDER BY borrow_date DESC");
    }

    public List<Map<String, Object>> getBorrowsByUser(Long userId) {
        return jdbc.queryForList(
            "SELECT * FROM borrow_records WHERE user_id = ? ORDER BY borrow_date DESC", userId);
    }

    // ── Reports ────────────────────────────────────────────────────────────

    public List<Map<String, Object>> getReports() {
        return jdbc.queryForList("SELECT * FROM borrow_records ORDER BY borrow_date DESC");
    }
}