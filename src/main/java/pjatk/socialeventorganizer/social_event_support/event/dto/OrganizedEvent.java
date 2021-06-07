package pjatk.socialeventorganizer.social_event_support.event.dto;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "organized_event")
public class OrganizedEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organized_event")
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isPredefined;

    private String eventStatus;

    @ManyToOne
    @JoinColumn(name = "id_event_type", nullable = false)
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "id_customer", nullable = false)
    private Customer customer;


}
