package org.example.calc.web;

import org.example.calc.dto.OrderRequest;
import org.example.calc.models.Order;
import org.example.calc.models.User;
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
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public List<Order> myOrders(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login);
        return orderService.findUserOrders(user.getId());
    }

    @GetMapping("/unfinalized")
    public ResponseEntity<Order> myUnfinalizedOrder(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login);
        Optional<Order> order = orderService.findUnpaidOrderByUserId(user.getId());
        return order.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value)).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/order")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login);
        Order order = orderService.placeOrder(user.getId(), orderRequest.bookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelOrder(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login);
        orderService.cancelOrder(user.getId());
        return ResponseEntity.noContent().build();
    }
    /*@PostMapping("/finalize")
    public ResponseEntity<Order> finalizeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userService.findByLogin(login);
        Order order = orderService.finalizeOrder(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }*/
}
