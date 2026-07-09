package org.example.calc.repositories.impl;

import org.example.calc.models.Order;
import org.example.calc.repositories.OrderJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryJpaAdapter {

    private final OrderJpaRepository delegate;

    public OrderRepositoryJpaAdapter(OrderJpaRepository delegate) { this.delegate = delegate; }

    public List<Order> findAll() {
        return delegate.findAll();
    }
    public Order save(Order order) {
        if(order.getId() == null) {
            order.setId(UUID.randomUUID().toString());
        }
        return delegate.save(order);
    }
    public Optional<Order> findById(String id) {
        return delegate.findById(id);
    }
    public void deleteById(String id) {
        delegate.deleteById(id);
    }
    public Optional<Order> findUnfinalizedOrderByBookId(String id) {
        Optional<Order> order = delegate.findAll().stream()
                .filter(o -> Objects.equals(o.getBookId(), id))
                .filter(o -> o.getFinalizeDateTime()==null).findFirst();
        return order.flatMap(value -> delegate.findById(value.getId()));
    }
}
