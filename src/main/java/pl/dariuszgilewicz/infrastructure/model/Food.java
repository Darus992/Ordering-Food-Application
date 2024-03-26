package pl.dariuszgilewicz.infrastructure.model;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Food {

    private Integer foodId;
    private String category;
    private String name;
    private String description;
    private BigDecimal price;
}
