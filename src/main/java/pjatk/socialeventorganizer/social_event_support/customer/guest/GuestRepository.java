package pjatk.socialeventorganizer.social_event_support.customer.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query(value = "select g.id_guest, g.first_name, g.last_name, g.email, g.id_customer from customer c join guest g on g.id_customer = c.id_customer_user join organized_event oe on oe.id_customer = c.id_customer_user join organized_event_guest oeg on g.id_guest = oeg.id_guest where oe.id_organized_event=:id", nativeQuery = true)
    Optional<List<Guest>> findInvitedGuestsByOrganizedEventId(@Param("id") Long id);
}
