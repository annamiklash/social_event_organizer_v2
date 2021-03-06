package pjatk.socialeventorganizer.social_event_support.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM customer c LEFT JOIN c.user cu " +
            "WHERE (:keyword is null or c.firstName LIKE %:keyword%) " +
            "OR (:keyword is null or c.lastName LIKE %:keyword%) " +
            "OR (:keyword is null or cu.email LIKE %:keyword%)")
    Page<Customer> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT c FROM customer c " +
            "left join fetch c.guests cg " +
            "WHERE c.id = :id")
    Optional<Customer> getByIdWithAllGuests(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "left join fetch c.user cu " +
            "left join fetch cu.appProblems cup " +
            "WHERE c.id = :id")
    Optional<Customer> getByIdWithProblems(@Param("id") long id);

    @Query("SELECT c from customer c " +
            "left join fetch c.user cu " +
            "left join fetch c.events ce " +
            "left join fetch ce.eventType cee " +
            "WHERE c.id = :id ")
    Optional<Customer> getByIdWithEvents(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "left join fetch c.guests cg " +
            "left join fetch c.user cu " +
            "left join fetch cu.appProblems cup " +
            "left join fetch c.events ce " +
            "left join fetch ce.eventType cee " +
            "WHERE c.id = :id")
    Optional<Customer> getWithDetail(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN FETCH c.user u " +
            "LEFT JOIN FETCH c.guests g " +
            "LEFT JOIN FETCH c.events " +
            "WHERE c.id = :id")
    Optional<Customer> getAllCustomerInformation(@Param("id") long id);
}
