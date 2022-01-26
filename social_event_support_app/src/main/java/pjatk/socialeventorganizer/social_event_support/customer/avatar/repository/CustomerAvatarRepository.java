package pjatk.socialeventorganizer.social_event_support.customer.avatar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar;

import java.util.Optional;

@Repository
public interface CustomerAvatarRepository extends JpaRepository<CustomerAvatar, Long> {

    Optional<CustomerAvatar> findCustomerAvatarByCustomer_Id(long id);
}
