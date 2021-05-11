package pjatk.socialeventorganizer.social_event_support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.model.dto.Business;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findBusinessByEmailAndPassword(String email, String password);


    @Query(value = "select id_user from business where email = :email", nativeQuery = true)
    long getBusinessUserId(@Param("email")String email);

    @Query(value = "select id_business from business where email = :email", nativeQuery = true)
    long getBusinessBusinessID(@Param("email") String email);

}
