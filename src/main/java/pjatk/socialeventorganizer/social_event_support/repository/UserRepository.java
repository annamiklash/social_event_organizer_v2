package pjatk.socialeventorganizer.social_event_support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pjatk.socialeventorganizer.social_event_support.model.dto.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query(value = "select u from users u where u.email = :email")
    Optional<User> findDistinctByEmail(@Param("email") String email);
}
