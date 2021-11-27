package pjatk.socialeventorganizer.social_event_support.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.location.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT distinct l.* from location l " +
            "left join address a on l.id_location_address = a.id_address " +
            "left join location_availability la on la.id_location = l.id_location " +
            "left join location_description ld on l.id_location = ld.id_location " +
            "left join description_item di on ld.name = di.name " +
            "WHERE la.status = 'AVAILABLE' " +
            "AND l.deleted_at IS NOT NULL " +
            "AND (:date is null or la.date = CAST(:date as timestamp)) " +
            "AND (:timeFrom is null or la.time_from <= CAST(:timeFrom as timestamp)) " +
            "AND (:timeTo is null or la.time_to >= CAST(:timeTo as timestamp))", nativeQuery = true)
    List<Location> search(@Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    @Query("SELECT l FROM location l " +
            "left join fetch l.business lb " +
            "left join fetch l.caterings lc " +
            "left join fetch l.descriptions ld " +
            "left join fetch l.locationBusinessHours lbh " +
            "left join fetch l.availability la where l.id = :id")
    Optional<Location> getByIdWithDetail(@Param("id") Long id);


    @Query("SELECT l FROM location AS l " +
            "WHERE l.name LIKE %:keyword% " +
            "OR  l.description LIKE %:keyword%")
    Page<Location> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

    @Query(value = "SELECT distinct l.* from location l " +
            "left join location_availability la on la.id_location = l.id_location " +
            "WHERE l.id_location = :id AND la.date = CAST(:date as timestamp)", nativeQuery = true)
    Optional<Location> getByIdWithAvailability(@Param("id") long id, @Param("date") String date);

    List<Location> findByLocationAddress_City(String city);
}
