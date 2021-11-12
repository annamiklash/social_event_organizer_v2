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
    List<Location> findAllByCateringId(@Param("cateringId") long cateringId);

//    @Query("SELECT l from location l left join fetch l.descriptions ld " +
//            "WHERE ld.id IN :ids AND l.name LIKE %:location% " +
//            "OR l.locationAddress.city LIKE %:location% OR l.locationAddress.country LIKE %:location%")
//    List<Location> search(@Param("ids") List<String> ids, @Param("location") String location);

    @Query("SELECT l from location l " +
            "left join fetch l.descriptions ls " +
            "left join fetch l.locationAvailability la " +
            "where (:location is null or l.name LIKE %:location%) " +
            " OR (:location is null or l.locationAddress.city LIKE %:location%) " +
            " OR (:location is null or l.locationAddress.country LIKE %:location%) " +
            " AND (:date is null or la.date = to_timestamp(:date, 'YYYY-MM-DD HH:mi'))" +
            "AND (:timeFrom is null or la.timeFrom = to_timestamp(:timeFrom, 'YYYY-MM-DD HH:mi')) " +
            "AND (:timeTo  is null or la.timeTo = to_timestamp(:timeFrom, 'YYYY-MM-DD HH:mi'))")
    List<Location> search(@Param("location") String location, @Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    @Query("SELECT l FROM location l " +
            "left join fetch l.business lb " +
            "left join fetch l.caterings lc " +
            "left join fetch l.descriptions ld " +
            "left join fetch l.locationAvailability la where l.id = :id")
    Optional<Location> getByIdWithDetail(@Param("id") Long id);

    List<Location> findByLocationAddress_City(String city);

    @Query("SELECT l FROM location AS l WHERE LOWER(l.name) LIKE %:keyword% OR LOWER(l.description) LIKE %:keyword%")
    Page<Location> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

    @Query("SELECT l from location l left join fetch l.locationAvailability la WHERE l.id = :id")
    Optional<Location> getByIdWithAvailability(@Param("id") long id);

}
