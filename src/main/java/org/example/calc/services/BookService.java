package org.example.calc.services;

import org.example.calc.models.Book;
import org.example.calc.repositories.impl.BookRepositoryJpaAdapter;
import org.example.calc.repositories.impl.OrderRepositoryJpaAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    private final BookValidator bookValidator;
    private final BookRepositoryJpaAdapter bookRepository;
    private final OrderRepositoryJpaAdapter orderRepository;

    public BookService(BookValidator bookValidator, BookRepositoryJpaAdapter bookRepository, OrderRepositoryJpaAdapter orderRepository) {
        this.bookValidator = bookValidator;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAllBooks() { return bookRepository.findAll(); }

    @Transactional(readOnly = true)
    public List<Book> findAvailableBooks() { return bookRepository.findAllAvailable(); }

    @Transactional(readOnly = true)
    public List<Book> findAuthorsBooks(String name) { return bookRepository.findByAuthor(name); }

    @Transactional(readOnly = true)
    public List<Book> findGenreBooks(String name) { return bookRepository.findByGenre(name); }

    @Transactional(readOnly = true)
    public Book findById(String id) {
        Optional<Book> b = bookRepository.findById(id);
        return b.orElse(null);
    }
    @Transactional(readOnly = true)
    public Book findByTitle(String title) {
        Optional<Book> b = bookRepository.findByTitle(title);
        return b.orElse(null);
    }
    @Transactional
    public void changeBookQuantity(String id, int amount) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Nie ma książki o takim id."));
            int newQuantity = book.getQuantity()+amount;
            if(newQuantity<0) {
                throw new IllegalArgumentException("Nie ma wystarczająco sztuk w magazynie.");
            } else {
                book.setQuantity(newQuantity);
            }
        bookRepository.save(book);
    }
    @Transactional
    public Book updatePrice(String id, Double price) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Nie ma książki o takim id."));
        book.setPrice(price);
        return bookRepository.save(book);
    }
    @Transactional
    public Book addBook(Book book) {
        bookValidator.validate(book);
        if(bookRepository.bookAlreadyExists(book)) {
            throw new IllegalArgumentException("Dana książka już jest w bazie danych.");
        }
        return bookRepository.save(book);
    }
    @Transactional
    public Book addSuggestedBook(Book book) {
        book.setQuantity(0);
        book.setPrice(9999);
        bookValidator.validate(book);
        if(bookRepository.bookAlreadyExists(book)) {
            throw new IllegalArgumentException("Dana książka już jest w bazie danych.");
        }
        return bookRepository.save(book);
    }
    @Transactional
    public void removeBook(String id) {
        if(orderRepository.findUnfinalizedOrderByBookId(id).isEmpty()) {
            bookRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Istnieje klient z zamówieniem na tę książkę.");
        }
    }
}
