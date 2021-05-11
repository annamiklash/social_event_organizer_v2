package pjatk.socialeventorganizer.social_event_support.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
