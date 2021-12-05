package pjatk.socialeventorganizer.social_event_support.catering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CateringRepository extends JpaRepository<Catering, Long> {

    List<Catering> findByCateringAddress_City(String city);

    @Query("SELECT c FROM catering AS c " +
            "JOIN c.business cb join cb.user cbu " +
            "WHERE  c.name LIKE %:keyword% " +
            "OR c.description LIKE %:keyword% " +
            "AND c.deletedAt IS NOT NULL " +
            "AND cbu.isActive = true")
    Page<Catering> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT c from catering c " +
            "left join fetch c.cateringItems ci " +
            "left join fetch c.availability ca " +
            "left join fetch c.cateringBusinessHours cbh " +
            "left join fetch c.locations cl " +
            "WHERE c.id = :id")
    Optional<Catering> findByIdWithDetail(@Param("id") long id);


    @Query(value = "SELECT distinct c.* from catering c " +
            "left join catering_availability ca on ca.id_catering = c.id_catering " +
            "where c.id_catering = :id AND ca.date = CAST(:date as timestamp)", nativeQuery = true)
    Optional<Catering> getByIdWithAvailability(@Param("id") long id, @Param("date") String date);

    @Query("SELECT distinct c from catering c left join c.cuisines cu " +
            "WHERE  c.name LIKE %:keyword% " +
            "AND (cu.id IN :cuisines)")
    List<Catering> search(@Param("cuisines") Set<Long> cuisines, @Param("keyword") String keyword);

    @Query("SELECT cat FROM catering cat " +
            "LEFT JOIN FETCH cat.cateringBusinessHours bh " +
            "WHERE cat.id = :cateringId")
    Optional<Catering> getWithBusinessHours(@Param("cateringId") long cateringId);

}
