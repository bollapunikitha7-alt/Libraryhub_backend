package com.libraryhub.cs.controller;

import com.libraryhub.cs.entity.Book;
import com.libraryhub.cs.entity.Category;
import com.libraryhub.cs.repository.CategoryRepository;
import com.libraryhub.cs.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "http://localhost:8081")
public class CategoryController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    // ── BOOK ENDPOINTS ────────────────────────────────────────────────

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/books/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String query) {
        return ResponseEntity.ok(bookService.searchBooks(query));
    }

    @GetMapping("/books/category/{categoryId}")
    public ResponseEntity<List<Book>> getBooksByCategory(
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryId));
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(
            @RequestBody Book book,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(bookService.addBook(book, categoryId));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestBody Book book,
            @RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(bookService.updateBook(id, book, categoryId));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ── CATEGORY ENDPOINTS ────────────────────────────────────────────

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Category not found with id=" + id)));
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(
            @RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(categoryRepository.save(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestBody Category updated) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Category not found with id=" + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        return ResponseEntity.ok(categoryRepository.save(existing));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}