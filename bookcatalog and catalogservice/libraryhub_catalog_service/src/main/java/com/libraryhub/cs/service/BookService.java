package com.libraryhub.cs.service;

import com.libraryhub.cs.entity.Book;
import com.libraryhub.cs.entity.Category;
import com.libraryhub.cs.repository.BookRepository;
import com.libraryhub.cs.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Book addBook(Book book, Long categoryId) {

        if (book.getIsbn() != null && !book.getIsbn().isBlank()
                && bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException(
                "A book with ISBN " + book.getIsbn() + " already exists.");
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Category not found with id=" + categoryId));
            book.setCategory(category);
        }

        book.setAvailableCopies(book.getTotalCopies());
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Book not found with id=" + id));
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.searchByKeyword(keyword.trim());
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    @Transactional
    public Book updateBook(Long id, Book updated, Long categoryId) {
        Book existing = getBookById(id);

        if (updated.getIsbn() != null && !updated.getIsbn().isBlank()
                && !updated.getIsbn().equals(existing.getIsbn())
                && bookRepository.existsByIsbn(updated.getIsbn())) {
            throw new IllegalArgumentException(
                "ISBN " + updated.getIsbn() + " is used by another book.");
        }

        if (updated.getTotalCopies() != null
                && !updated.getTotalCopies().equals(existing.getTotalCopies())) {
            int borrowed     = existing.getTotalCopies() - existing.getAvailableCopies();
            int newAvailable = updated.getTotalCopies() - borrowed;
            if (newAvailable < 0) {
                throw new IllegalArgumentException(
                    "Cannot reduce totalCopies — "
                    + borrowed + " copies are currently borrowed.");
            }
            existing.setTotalCopies(updated.getTotalCopies());
            existing.setAvailableCopies(newAvailable);
        }

        if (updated.getTitle()  != null) existing.setTitle(updated.getTitle());
        if (updated.getAuthor() != null) existing.setAuthor(updated.getAuthor());
        if (updated.getIsbn()   != null) existing.setIsbn(updated.getIsbn());
        if (updated.getGenre()  != null) existing.setGenre(updated.getGenre());

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Category not found with id=" + categoryId));
            existing.setCategory(category);
        }

        return bookRepository.save(existing);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        int borrowed = book.getTotalCopies() - book.getAvailableCopies();
        if (borrowed > 0) {
            throw new IllegalStateException(
                "Cannot delete — " + borrowed + " copy/copies currently borrowed.");
        }
        bookRepository.delete(book);
    }
}