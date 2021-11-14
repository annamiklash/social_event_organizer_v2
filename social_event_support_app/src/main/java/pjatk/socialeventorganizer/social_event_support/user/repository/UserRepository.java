package pjatk.socialeventorganizer.social_event_support.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.user.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from users u right join business b on b.id = u.id where u.id = :id AND u.type = :type")
    Optional<User> isNewAccount(@Param("id") long id, @Param("type") char type);

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);

    User findUserByResetPasswordToken(String resetPasswordToken);
}
