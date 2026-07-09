package org.example.calc.services;

import org.example.calc.models.Book;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BookValidator {
    private void requireNonBlank(String value, String message) {
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
    private void validateBaseFields(Book book) {
        requireNonBlank(book.getAuthor(), "Autor jest wymagany");
        requireNonBlank(book.getTitle(), "Tytuł jest wymagany");
        requireNonBlank(book.getGenre(), "Gatunek jest wymagany");

        if(book.getYear() <= 0) throw new IllegalArgumentException("Rok musi być dodatni.");
        if(book.getPrice() <= 0) throw new IllegalArgumentException("Cena musi być dodatnia.");
        if(book.getQuantity() < 0) throw new IllegalArgumentException("Ilość nie może być ujemna.");
    }
    private void noTagRepetition(Book book) {
        Set<String> tags = new HashSet<>();

        for (String tag : book.getTags()) {
            requireNonBlank(tag, "Tag nie może być pustym napisem.");
            if (!tags.add(tag)) {
                throw new IllegalArgumentException("Tag "+tag+" się powtarza");
            }
        }
    }
    public void validate(Book book) {
        if (book == null) throw new IllegalArgumentException("Książka nie może być null.");
        validateBaseFields(book);
        noTagRepetition(book);
    }
}
