package pjatk.socialeventorganizer.social_event_support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.model.dto.LocationImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationImageRepository extends JpaRepository<LocationImage, Long> {

    Optional<List<LocationImage>> findAllByLocationId(Integer locationId);
}
