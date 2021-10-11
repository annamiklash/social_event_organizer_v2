package pjatk.socialeventorganizer.social_event_support.appproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.dto.AppProblem;

import java.util.List;

@Repository
public interface AppProblemRepository extends JpaRepository<AppProblem, Long> {

    boolean existsById(Long id);

    List<AppProblem> findByUserId(Integer userId);
}
