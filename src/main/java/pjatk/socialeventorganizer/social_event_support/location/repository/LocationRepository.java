package pjatk.socialeventorganizer.social_event_support.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.location.model.dto.Location;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "select l.id_location from location_description l where l.name in (:descriptionItems) GROUP BY l.id_location having count (*) = :size", nativeQuery = true)
    List<Integer> filterByDescriptions(@Param("descriptionItems") List<String> descriptionItems, @Param("size") long size);

    @Modifying
    @Query(value = "insert into catering_location (id_location, id_catering) values (:locationId, :cateringId)", nativeQuery = true)
    void addLocationToCatering(@Param("locationId") Long locationId, @Param("cateringId") Long cateringId);

    List<Location> findByNameContaining(String name);

    List<Location> findByLocationAddress_City(String city);

}
