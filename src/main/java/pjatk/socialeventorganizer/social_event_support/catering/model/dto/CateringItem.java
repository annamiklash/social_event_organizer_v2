package pjatk.socialeventorganizer.social_event_support.catering.model.dto;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
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
    String itemType;

    @Column(name = "id_catering")
    Integer cateringId;

}


