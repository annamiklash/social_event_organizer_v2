package pjatk.socialeventorganizer.social_event_support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.model.dto.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
