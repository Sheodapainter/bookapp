package org.example.calc.services;

import org.example.calc.models.Book;
import org.example.calc.models.Order;
import org.example.calc.models.User;
import org.example.calc.repositories.impl.BookRepositoryJpaAdapter;
import org.example.calc.repositories.impl.OrderRepositoryJpaAdapter;
import org.example.calc.repositories.impl.UserRepositoryJpaAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    private final BookRepositoryJpaAdapter bookRepository;
    private final UserRepositoryJpaAdapter userRepository;
    private final OrderRepositoryJpaAdapter orderRepository;

    public OrderService(BookRepositoryJpaAdapter bookRepository, UserRepositoryJpaAdapter userRepository, OrderRepositoryJpaAdapter orderRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order placeOrder(String userId, String bookId) {
        boolean unfinalizedOrder = orderRepository.findAll().stream().anyMatch(o -> userId.equals(o.getUserId()) && o.isUnpaid());
        if(unfinalizedOrder) {
            throw new IllegalStateException("Masz nieopłacone zamówienie.");
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono książki o podanym id."));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));

        boolean bookIsAvailable = book.takeOne();
        if(!bookIsAvailable) {
            throw new IllegalStateException("Nie ma tej książki w magazynie.");
        }

        Order order = Order.builder()
                .user(user)
                .book(book)
                .placeDateTime(LocalDateTime.now().toString())
                .finalizeDateTime(null)
                .build();

        return orderRepository.save(order);
    }

    @Transactional
    public Order finalizeOrder(String userId) {
        Order order = orderRepository.findAll().stream()
                .filter(o -> o.getUserId().equals(userId))
                .filter(Order::isUnpaid)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nie masz aktualnie nieopłaconych zamówień."));

        order.setFinalizeDateTime(LocalDateTime.now().toString());

        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(String userId) {
        Order order = orderRepository.findAll().stream()
                .filter(o -> o.getUserId().equals(userId))
                .filter(Order::isUnpaid)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nie masz aktualnie nieopłaconych zamówień."));
        Optional<Book> orderedBook = bookRepository.findById(order.getBookId());
        orderedBook.ifPresent(book -> book.addQuantity(1));
        orderRepository.deleteById(order.getId());
    }

    @Transactional(readOnly = true)
    public Optional<Order> findUnpaidOrderByUserId(String userId) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getUserId().equals(userId))
                .filter(Order::isUnpaid)
                .findFirst();
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrders() { return orderRepository.findAll(); }

    @Transactional(readOnly = true)
    public List<Order> findUserOrders(String userId) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getUserId().equals(userId))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean userHasUnpaidOrder(String userId) { return findUnpaidOrderByUserId(userId).isPresent(); }
}
