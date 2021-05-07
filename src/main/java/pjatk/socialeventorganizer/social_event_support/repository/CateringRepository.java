package pjatk.socialeventorganizer.social_event_support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.model.dto.Catering;

import java.util.List;

@Repository
public interface CateringRepository extends JpaRepository<Catering, Long> {


    List<Catering> findByNameContaining(String name);

    List<Catering> findByCateringAddress_City(String city);

    @Modifying
    @Query(value = "insert into catering_location (id_catering, id_location) values (:cateringId, :locationId)", nativeQuery = true)
    void addCateringToLocation(@Param("locationId") Long locationId, @Param("cateringId") Long cateringId);

}
