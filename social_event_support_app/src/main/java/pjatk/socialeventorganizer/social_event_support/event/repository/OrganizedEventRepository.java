package pjatk.socialeventorganizer.social_event_support.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.event.model.OrganizedEvent;

@Repository
public interface OrganizedEventRepository extends JpaRepository<OrganizedEvent, Long> {

//    @Query("SELECT oe FROM organized_event oe " +
//            "LEFT JOIN fetch oe.customer oec " +
//            "LEFT JOIN fetch oec.user u " +
//            "WHERE LOWER(oe.name) LIKE %:keyword%")
//    Page<OrganizedEvent> findAllWithKeyword(Pageable pageable);

//    List<OrganizedEvent> getAllWithCustomerAndLocation();



}
