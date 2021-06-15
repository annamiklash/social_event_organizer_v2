package pjatk.socialeventorganizer.social_event_support.serviceforevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.serviceforevent.ServiceForEvent;

import java.util.List;

@Repository
public interface ServiceForEventRepository extends JpaRepository<ServiceForEvent, Long> {

    @Query("select sfe " +
            "from service_for_event sfe " +
            "join location_for_event lfe on lfe.id = sfe.locationforevent.id " +
            "join optional_service os on os.id = sfe.optionalService.id " +
            "where lfe.id=:id")
    List<ServiceForEvent> findServiceForEventByLocationForEventId(@Param("id") Long id);

}
