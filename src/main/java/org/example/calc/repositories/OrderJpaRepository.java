package org.example.calc.repositories;

import org.example.calc.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, String> {
}
