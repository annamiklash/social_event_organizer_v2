package pjatk.socialeventorganizer.social_event_support.optionalService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.dto.OptionalService;

import java.util.List;

@Repository
public interface OptionalServiceRepository extends JpaRepository<OptionalService, Long> {

    List<OptionalService> findByAliasContaining(String alias);



}
