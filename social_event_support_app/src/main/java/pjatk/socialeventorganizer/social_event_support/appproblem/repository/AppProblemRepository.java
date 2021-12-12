package pjatk.socialeventorganizer.social_event_support.appproblem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.appproblem.model.AppProblem;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppProblemRepository extends JpaRepository<AppProblem, Long> {

    @Query("SELECT a FROM app_problem AS a " +
            "left join a.user ua " +
            "WHERE (:keyword is null or a.description LIKE %:keyword%) " +
            "OR (:keyword is null or a.concern LIKE %:keyword%)")
    Page<AppProblem> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);


    @Query("SELECT a FROM app_problem AS a " +
            "left join fetch a.user ua " +
            "WHERE a.id = :id")
    Optional<AppProblem> findByIdWithDetail(@Param("id") long id);

    List<AppProblem> findByUser_Id(long id);

}
