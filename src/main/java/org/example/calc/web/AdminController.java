package org.example.calc.web;

import org.example.calc.models.Book;
import org.example.calc.models.Order;
import org.example.calc.models.User;
import org.example.calc.services.BookService;
import org.example.calc.services.OrderService;
import org.example.calc.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final BookService bookService;
    private final OrderService orderService;
    private final UserService userService;
    public AdminController(BookService bookService, OrderService orderService, UserService userService) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.userService = userService;
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login);
        userService.deleteUser(userId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders")
    public List<Order> orderHistory() { return orderService.findAllOrders(); }

    @GetMapping("/orders/{id}")
    public List<Order> userOrderHistory(@PathVariable String id) { return orderService.findUserOrders(id); }

    @PostMapping("/orders/finalize/{userId}")
    public ResponseEntity<Order> finalizeOrder(@PathVariable String userId) {
        Order order = orderService.finalizeOrder(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/books/restock/{id}/{amount}")
    public ResponseEntity<Void> restockBook(@PathVariable String id, @PathVariable int amount) {
        bookService.changeBookQuantity(id, amount);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("books/add")
    public Book add(@RequestBody Book book) { return bookService.addBook(book); }

    @PostMapping("books/price/{id}/{price}")
    public Book updatePrice(@PathVariable String id, @PathVariable Double price) { return bookService.updatePrice(id, price); }

    @DeleteMapping("books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        bookService.removeBook(id);
        return ResponseEntity.noContent().build();
    }
}
