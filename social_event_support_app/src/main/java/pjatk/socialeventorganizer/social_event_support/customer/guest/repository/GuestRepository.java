package pjatk.socialeventorganizer.social_event_support.customer.guest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.customer.guest.model.Guest;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query(value = "select g.id_guest, g.first_name, g.last_name, g.email, g.id_customer from customer c join guest g on g.id_customer = c.id_customer_user join organized_event oe on oe.id_customer = c.id_customer_user join organized_event_guest oeg on g.id_guest = oeg.id_guest where oe.id_organized_event=:id", nativeQuery = true)
    Optional<List<Guest>> findInvitedGuestsByOrganizedEventId(@Param("id") Long id);

    @Query("SELECT g FROM guest g WHERE LOWER(g.firstName) LIKE %:keyword% OR LOWER(g.lastName) LIKE %:keyword% OR LOWER(g.email) LIKE %:keyword%")
    Page<Guest> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    List<Guest> getAllByCustomer_Id(long id);
}
