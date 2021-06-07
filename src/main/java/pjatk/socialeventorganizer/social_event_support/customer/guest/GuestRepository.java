package pjatk.socialeventorganizer.social_event_support.customer.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query("select g " +
            "from guest g " +
            "join customer c on c.id = g.customerId " +
            "join organized_event oe on oe.customer.id = c.id " +
            "where oe.id=:id")
    Optional<List<Guest>> findGuestsByOrganizedEventId(@Param("id") Long id);
}
