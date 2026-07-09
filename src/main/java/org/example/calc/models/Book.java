package org.example.calc.models;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(nullable = false, unique = true)
    private String id;

    private String genre;
    private String title;
    private String author;

    @Column(columnDefinition = "NUMERIC")
    private int year;

    @Column(columnDefinition = "NUMERIC")
    private double price;

    @Column(columnDefinition = "NUMERIC")
    private int quantity;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<String> tags = new ArrayList<>();


    @Builder
    public Book (String id, String genre, String title, String author, int year, double price, int quantity, List<String> tags) {
        this.id=id;
        this.genre=genre;
        this.title=title;
        this.author=author;
        this.year=year;
        this.price=price;
        this.quantity=quantity;
        this.tags=tags==null?new ArrayList<>() : new ArrayList<>(tags);
    }
    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }
    public void addTag(String tag) {
        tags.add(tag);
    }
    public void removeTag(String name) {
        tags.remove(name);
    }
    public boolean takeOne() {
        if(quantity>0) {
            quantity--;
            return true;
        } else {
            return false;
        }
    }
    public void addQuantity(int number) {
        quantity+=number;
    }
    public Book copy() {
        return Book.builder()
                .id(id)
                .genre(genre)
                .title(title)
                .author(author)
                .year(year)
                .price(price)
                .quantity(quantity)
                .tags(new ArrayList<>(tags))
                .build();
    }
}
