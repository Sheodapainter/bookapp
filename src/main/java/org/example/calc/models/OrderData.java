package org.example.calc.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString
public class OrderData {
    private String id;
    private String bookId;
    private String userId;
    private String placeDateTime;
    private String finalizeDateTime;

    public OrderData copy() {
        return OrderData.builder()
                .id(id)
                .bookId(bookId)
                .userId(userId)
                .placeDateTime(placeDateTime)
                .finalizeDateTime(finalizeDateTime)
                .build();
    }
}
