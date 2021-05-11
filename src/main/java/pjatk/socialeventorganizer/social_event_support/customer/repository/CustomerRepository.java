package pjatk.socialeventorganizer.social_event_support.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.customer.model.dto.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    Optional<Customer> findByUserEmail(String email);


    Optional<Customer> findByUser_Id(Long id);

    Boolean existsByUser_Id(Long id);


}
