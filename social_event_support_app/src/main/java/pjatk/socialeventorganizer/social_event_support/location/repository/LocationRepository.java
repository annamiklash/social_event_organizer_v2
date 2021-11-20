package pjatk.socialeventorganizer.social_event_support.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "select l.id_location from location_description l where l.name in (:descriptionItems) GROUP BY l.id_location having count (*) = :size", nativeQuery = true)
    List<Integer> filterByDescriptions(@Param("descriptionItems") List<String> descriptionItems, @Param("size") long size);


    @Modifying
    @Query(value = "insert into catering_location (id_location, id_catering) values (:locationId, :cateringId)", nativeQuery = true)
    void addLocationToCatering(@Param("locationId") Long locationId, @Param("cateringId") Long cateringId);

    @Query("SELECT l FROM location l left join fetch l.caterings lc where lc.id = :cateringId")
    List<Integer> findAllByCateringId(@Param("cateringId") long cateringId);


    @Query(value = "SELECT distinct l.* from location l " +
            "left join location_availability la on la.id_location = l.id_location " +
            "left join location_description ld on l.id_location = ld.id_location " +
            "left join description_item di on ld.name = di.name " +
            "WHERE la.status = 'AVAILABLE' " +
            "AND la.date = CAST(:date as timestamp) " +
            "AND la.time_from <= CAST(:timeFrom as timestamp ) " +
            "AND la.time_to >= CAST(:timeTo as timestamp)", nativeQuery = true)
    List<Location> search(@Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);


    @Query("SELECT l FROM location l " +
            "left join fetch l.business lb " +
            "left join fetch l.caterings lc " +
            "left join fetch l.descriptions ld " +
            "left join fetch l.locationBusinessHours lbh " +
            "left join fetch l.LocationAvailability la where l.id = :id")
    Optional<Location> getByIdWithDetail(@Param("id") Long id);

    List<Location> findByLocationAddress_City(String city);

    @Query("SELECT l FROM location AS l WHERE LOWER(l.name) LIKE %:keyword% OR LOWER(l.description) LIKE %:keyword%")
    Page<Location> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

    @Query(value = "SELECT distinct l.* from location l " +
            "left join location_availability la on la.id_location = l.id_location " +
            "WHERE l.id_location = :id AND la.date = CAST(:date as timestamp)", nativeQuery = true)
    Optional<Location> getByIdWithAvailability(@Param("id") long id, @Param("date") String date);

}
