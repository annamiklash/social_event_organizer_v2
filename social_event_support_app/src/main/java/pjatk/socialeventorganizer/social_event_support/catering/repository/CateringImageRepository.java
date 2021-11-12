package pjatk.socialeventorganizer.social_event_support.catering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringImage;

@Repository
public interface CateringImageRepository extends JpaRepository<CateringImage, Long> {
}
