package pjatk.socialeventorganizer.social_event_support.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findBusinessByUser_EmailAndUser_Password(String email, String password);

    Boolean existsByUser_Id(Long id);

    @Query("SELECT b FROM business b left join fetch b.address ba left join fetch b.caterings bc left join fetch b.locations bl WHERE b.id = :id")
    Optional<Business> getWithDetail(@Param("id") long id);


    Optional<Business> findById(long id);
}
