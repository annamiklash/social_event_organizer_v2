package pjatk.socialeventorganizer.social_event_support.catering.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@EqualsAndHashCode(exclude="catering")
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_item")
@Table(name = "catering_item")
public class CateringItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_item")
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "serving_price")
    BigDecimal servingPrice;

    @Column(name = "is_vegan")
    boolean isVegan;

    @Column(name = "is_vegetarian")
    boolean isVegetarian;

    @Column(name = "is_gluten_free")
    boolean isGlutenFree;

    @Column(name = "type")
    private String itemType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering", nullable = false)
    private Catering catering;

}


