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

    Optional<Customer> getById(long id);

    Optional<Customer> findByUserEmail(String email);

    Optional<Customer> findByUser_Id(Long id);

    Boolean existsByUser_Id(Long id);

    @Query("SELECT c FROM customer c LEFT JOIN c.user cu " +
            "WHERE LOWER(c.firstName) LIKE %:keyword% " +
            "OR LOWER(c.lastName) LIKE %:keyword% " +
            "OR LOWER(cu.email) LIKE %:keyword%")
    Page<Customer> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    @Query("SELECT c FROM customer c left join fetch c.guests cg WHERE c.id = :id")
    Optional<Customer> getByIdWithAllGuests(@Param("id") long id);

    @Query("SELECT c FROM customer c left join fetch c.user cu left join fetch cu.appProblems cup WHERE c.id = :id")
    Optional<Customer> getByIdWithProblems(@Param("id") long id);

    @Query("SELECT c from customer c left join fetch c.user cu left join fetch c.events ce left join fetch ce.eventType cee WHERE c.id = :id ")
    Optional<Customer> getByIdWithEvents(@Param("id") long id);

    @Query("SELECT c FROM customer c " +
            "left join fetch c.guests cg " +
            "left join fetch c.user cu " +
            "left join fetch cu.appProblems cup left join fetch c.events ce left join fetch ce.eventType cee " +
            "WHERE c.id = :id")
    Optional<Customer> getWithDetail(@Param("id") long id);
}
