package org.example.calc.web;

import org.example.calc.models.Book;
import org.example.calc.services.BookService;
import org.example.calc.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final OrderService orderService;

    public BookController(BookService bookService, OrderService orderService) {
        this.bookService = bookService;
        this.orderService = orderService;
    }

    @GetMapping
    public List<Book> showAvailableBooks() { return bookService.findAvailableBooks(); }

    @GetMapping("/all")
    public List<Book> showAllBooks() { return bookService.findAllBooks(); }

    @GetMapping("/{id}")
    public Book get(@PathVariable String id) { return bookService.findById(id); }

    @GetMapping("/title/{title}")
    public Book getByTitle(@PathVariable String title) { return bookService.findByTitle(title); }

    @GetMapping("/author/{author}")
    public List<Book> getAuthors(@PathVariable String author) { return bookService.findAuthorsBooks(author); }

    @GetMapping("/genre/{genre}")
    public List<Book> getGenre(@PathVariable String genre) { return bookService.findGenreBooks(genre); }

    @PostMapping
    public Book suggest(@RequestBody Book book) { return bookService.addSuggestedBook(book); }
}
