package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_order_choice")
public class CateringOrderChoice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_catering_item")
    private Long id;

    @Min(1)
    @Column(name = "count", nullable = false)
    private int amount;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering_item")
    private CateringItem item;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_catering_for_chosen_location", nullable = false)
    private CateringForChosenEventLocation eventLocationCatering;

}
