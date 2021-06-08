package pjatk.socialeventorganizer.social_event_support.optionalService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.dto.OptionalServiceImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceImageRepository extends JpaRepository<OptionalServiceImage, Long> {

    Optional<List<OptionalServiceImage>> findAllByOptionalServiceId(Integer optionalServiceId);
}
