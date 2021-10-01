package pjatk.socialeventorganizer.social_event_support.appproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;
import pjatk.socialeventorganizer.social_event_support.business.model.dto.Business;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.Catering;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppProblemRepository extends JpaRepository<AppProblem, Long> {

//    Optional<Business> findBusinessByUser_EmailAndUser_Password(String email, String password);
//
//    List<Catering> findByNameContaining(String name);

//    List<AppProblem> findByUser(Long userId);


    //Boolean existsByUser_Id(Long id);

    List<AppProblem> findByUserId(Long userId);
}
