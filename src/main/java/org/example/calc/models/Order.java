package org.example.calc.models;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "place_date", nullable = false)
    private String placeDateTime;

    @Column(name = "finalize_date")
    private String finalizeDateTime;

    public Order copy() {
        return Order.builder()
                .id(id)
                .book(book)
                .user(user)
                .placeDateTime(placeDateTime)
                .finalizeDateTime(finalizeDateTime)
                .build();
    }

    public boolean isUnpaid() { return finalizeDateTime == null || finalizeDateTime.isBlank(); }
    public String getBookId() { return book == null ? null : book.getId(); }
    public String getUserId() { return user == null ? null : user.getId(); }
}
