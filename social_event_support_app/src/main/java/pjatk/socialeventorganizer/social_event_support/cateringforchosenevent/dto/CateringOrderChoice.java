package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "catering_order_choice")
public class CateringOrderChoice implements Serializable {

    @Id
    @Column(name = "id_catering_item")
    private Long id;

    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private CateringItem item;

    @ManyToOne
    @JoinColumn(name = "id_organized_event", nullable = false)
    CateringForChosenEventLocation eventLocationCatering;

}
