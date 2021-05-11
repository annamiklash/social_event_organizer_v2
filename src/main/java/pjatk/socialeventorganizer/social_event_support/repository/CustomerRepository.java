package pjatk.socialeventorganizer.social_event_support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.model.dto.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    Optional<Customer> findByEmailAndPassword(String email, String password);

    Optional<Customer> findByEmail(String email);


    Optional<Customer> getCustomerById(long id);

    @Query(value = "select id_user from customer where email = :email", nativeQuery = true)
    int getCustomerUserId(@Param("email") String email);

    @Query(value = "select id_customer from customer where email = :email", nativeQuery = true)
    int getCustomerCustomerIdId(@Param("email") String email);


}
