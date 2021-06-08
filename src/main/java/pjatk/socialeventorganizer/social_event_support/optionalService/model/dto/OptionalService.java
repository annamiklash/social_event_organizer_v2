package pjatk.socialeventorganizer.social_event_support.optionalService.model.dto;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "optional_service")
@Entity(name = "optional_service")
public class OptionalService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_optional_service")
    Long id;

    @Column
    String alias;

    @Column
    String type;

    @Column
    String email;

    @Column
    String description;

    @Column(name = "service_cost")
    BigDecimal serviceCost;

    @Column(name = "id_business")
    Integer businessId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_optional_service")
    Set<OptionalServiceImage> images = new HashSet<>();


}
