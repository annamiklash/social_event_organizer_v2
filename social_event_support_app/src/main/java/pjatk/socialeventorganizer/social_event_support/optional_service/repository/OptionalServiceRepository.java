package pjatk.socialeventorganizer.social_event_support.optional_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionalServiceRepository extends JpaRepository<OptionalService, Long> {

    @Query("SELECT os from optional_service os " +
            "WHERE os.type LIKE %:keyword% " +
            "OR os.description LIKE %:keyword% " +
            "OR os.alias LIKE %:keyword% " +
            "AND os.deletedAt is null")
    Page<OptionalService> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

    @Query(value = "SELECT os.* from optional_service os " +
            "left join optional_service_availability sa on sa.id_optional_service = os.id_optional_service " +
            "LEFT JOIN address a on os.id_service_address = a.id_address " +
            "WHERE sa.date = CAST(:date as timestamp) " +
            "AND os.deleted_at IS NULL " +
            "AND (:type like '' or os.type = :type) " +
            "AND (:city like '' or a.city = :city)", nativeQuery = true)
    List<OptionalService> search(@Param("date") String date, @Param("type") String type, @Param("city") String city);

    @Query(value = "SELECT distinct os.* from optional_service os " +
            "left join optional_service_availability sa on sa.id_optional_service = os.id_optional_service " +
            "LEFT JOIN address a on os.id_service_address = a.id_address " +
            "WHERE (:type like '' or os.type = :type) " +
            "AND (:city like '' or a.city = :city)", nativeQuery = true)
    List<OptionalService> searchByType(@Param("type") String type, @Param("city") String city);

    @Query(value = "SELECT distinct os.* from optional_service os " +
            "LEFT JOIN optional_service_availability osa ON osa.id_optional_service = os.id_optional_service " +
            "WHERE os.id_optional_service = :serviceId AND osa.date = CAST(:date as timestamp) " +
            "AND (:timeFrom is null or osa.time_from <= CAST(:timeFrom as timestamp)) " +
            "AND (:timeTo is null or osa.time_to >= CAST(:timeTo as timestamp))", nativeQuery = true)
    Optional<OptionalService> available(@Param("serviceId") long serviceId, @Param("date") String date, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

    @Query("SELECT os from optional_service os " +
            "LEFT JOIN FETCH os.styles ms " +
            "LEFT JOIN FETCH os.availability osa " +
            "LEFT JOIN FETCH os.optionalServiceBusinessHours bh " +
            "LEFT JOIN FETCH os.serviceForLocation sfl " +
            "LEFT JOIN FETCH sfl.locationForEvent lfe " +
            "LEFT JOIN FETCH lfe.event " +
            "WHERE os.id = :serviceId")
    Optional<OptionalService> getAllServiceInformation(@Param("serviceId") long serviceId);

    @Query("SELECT os from optional_service os " +
            "LEFT JOIN FETCH os.styles ms " +
            "LEFT JOIN FETCH os.availability osa " +
            "LEFT JOIN FETCH os.optionalServiceBusinessHours bh " +
            "WHERE os.id = :serviceId")
    Optional<OptionalService> findWithDetail(@Param("serviceId") long serviceId);

    List<OptionalService> findAllByBusiness_Id(long id);

    @Query(value = "SELECT os.* from optional_service os " +
            "left join optional_service_availability sa on sa.id_optional_service = os.id_optional_service " +
            "LEFT JOIN address a on os.id_service_address = a.id_address " +
            "WHERE sa.date = CAST(:date as timestamp) " +
            "AND (:city like '' or a.city like :city)", nativeQuery = true)
    List<OptionalService> searchByDate(@Param("date") String date, @Param("city") String city);

    @Query("SELECT distinct s from optional_service s " +
            "left join fetch s.serviceAddress sa " +
            "left join fetch s.availability " +
            "left join fetch s.optionalServiceBusinessHours bh " +
            "WHERE s.deletedAt IS NULL")
    List<OptionalService> getAll();

}
