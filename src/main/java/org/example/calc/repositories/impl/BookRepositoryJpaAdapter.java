package org.example.calc.repositories.impl;

import org.example.calc.models.Book;
import org.example.calc.repositories.BookJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookRepositoryJpaAdapter {

    private final BookJpaRepository delegate;

    public BookRepositoryJpaAdapter(BookJpaRepository delegate) { this.delegate = delegate; }

    public List<Book> findAll() {
        return delegate.findAll();
    }
    public List<Book> findAllAvailable() {
        return delegate.findAll().stream().filter(b -> b.getQuantity()>0).toList();
    }
    public List<Book> findByAuthor(String author) {
        return delegate.findAll().stream().filter(b -> Objects.equals(b.getAuthor(), author)).toList();
    }
    public List<Book> findByGenre(String genre) {
        return delegate.findAll().stream().filter(b -> Objects.equals(b.getGenre(), genre)).toList();
    }
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID().toString());
        }
        return delegate.save(book);
    }
    public Optional<Book> findById(String id) {
        return delegate.findById(id);
    }
    public Optional<Book> findByTitle(String title) {
        Optional<Book> user = delegate.findAll().stream().filter(b -> Objects.equals(b.getTitle(), title)).findFirst();
        return user.flatMap(value -> delegate.findById(value.getId()));
    }
    public void deleteById(String id) {
        delegate.deleteById(id);
    }
    public boolean bookAlreadyExists(Book book) {
        return delegate.findAll().stream().anyMatch(b ->
                b.getTitle().equals(book.getTitle()) &&
                b.getAuthor().equals(book.getAuthor()) &&
                b.getGenre().equals(book.getGenre()) &&
                b.getYear()==book.getYear());
    }
}
