package pjatk.socialeventorganizer.social_event_support.cuisine.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "cuisine")
@Entity(name = "cuisine")
public class Cuisine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuisine")
    private Long id;

    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "cuisines", fetch = FetchType.LAZY)
    private Set<Catering> caterings = new HashSet<>();
}
