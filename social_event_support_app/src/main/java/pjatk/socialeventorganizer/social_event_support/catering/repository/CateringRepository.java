package pjatk.socialeventorganizer.social_event_support.catering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;

import java.util.List;
import java.util.Optional;

@Repository
public interface CateringRepository extends JpaRepository<Catering, Long>, JpaSpecificationExecutor<Catering> {

    List<Catering> findByNameContaining(String name);

    List<Catering> findByCateringAddress_City(String city);

    @Modifying
    @Query(value = "insert into catering_location (id_catering, id_location) values (:cateringId, :locationId)", nativeQuery = true)
    void addCateringToLocation(@Param("locationId") Long locationId, @Param("cateringId") Long cateringId);

    @Query("SELECT c FROM catering AS c join c.business cb join cb.user cbu " +
            "WHERE LOWER(c.name) LIKE %:keyword% " +
            "OR LOWER(c.description) LIKE %:keyword% " +
            "AND c.deletedAt IS NOT NULL " +
            "AND cbu.isActive = true")
    Page<Catering> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT c from catering c left join fetch c.cateringItems ci WHERE c.id = :id")
    Optional<Catering> findByIdWithDetail(@Param("id") long id);


//    @Query("SELECT c FROM catering AS c WHERE c.name LIKE %:keyword% OR c.description LIKE %:keyword%")
//    List<Catering> search(@Param("keyword") String keyword);

//
//    @Query(value = "SELECT e FROM EmployeeProjectView as e WHERE e.employeeId=:employeeId and (:inputString is null or e.lastName like %:inputString% ) and " +
//            "(:inputString is null or e.firstName like %:inputString%) and (:inputString is null or concat(e.projectId,'') like %:inputString%) and " +
//            " (:inputString is null or e.projectName like %:inputString%) and  (:inputString is null or concat(e.projectBudget,'') like %:inputString%) and "+
//            " (:inputString is null or e.projectLocation like %:inputString%)"
//    )
//    Page<EmployeeProjectView> findAllByInputString(Long employeeId, String inputString, Pageable pageable);


}
