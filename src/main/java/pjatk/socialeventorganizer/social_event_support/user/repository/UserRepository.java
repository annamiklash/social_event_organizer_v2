package pjatk.socialeventorganizer.social_event_support.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
