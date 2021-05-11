package pjatk.socialeventorganizer.social_event_support.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.Business;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findBusinessByUser_EmailAndUser_Password(String email, String password);

    Optional<Business> findByUserEmail(String email);

    Optional<Business> findByUser_Id(Long id);

    Boolean existsByUser_Id(Long id);

}
