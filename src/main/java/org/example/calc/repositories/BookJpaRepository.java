package org.example.calc.repositories;

import org.example.calc.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookJpaRepository extends JpaRepository<Book, String> {
}
