package pjatk.socialeventorganizer.social_event_support.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.LocationDescriptionItem;

@Repository
public interface LocationDescriptionItemRepository extends JpaRepository<LocationDescriptionItem, String> {


     LocationDescriptionItem getLocationDescriptionItemById(String id);

     @Query("select di from description_item di where di.id = :name ")
     LocationDescriptionItem getLocationDescriptionItemByName(@Param("name") String name);


}
