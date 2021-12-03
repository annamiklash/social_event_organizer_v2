package pjatk.socialeventorganizer.social_event_support.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;

import java.util.Optional;

@Repository
public interface OrganizedEventRepository extends JpaRepository<OrganizedEvent, Long> {


    @Query("SELECT oe from organized_event oe " +
            "left join fetch oe.eventType et " +
            "left join fetch oe.customer c " +
            "left join fetch c.user cu " +
            "left join fetch oe.locationForEvent lfe " +
            "left join fetch lfe.cateringsForEventLocation cfl " +
            "left join fetch cfl.catering cflc " +
            "left join fetch lfe.location l " +
            "left join fetch l.locationAddress la " +
            "left join fetch oe.guests g WHERE oe.id = :id")
    Optional<OrganizedEvent> getWithAllInformationForSendingInvitations(@Param("id") long id);


    boolean existsOrganizedEventByIdAndCustomer_Id(long eventId, long customerId);

}
