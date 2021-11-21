package pjatk.socialeventorganizer.social_event_support.cuisine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine;

@Repository
public interface CuisineRepository extends JpaRepository<Cuisine, Long> {

    boolean existsByName(String name);
}
