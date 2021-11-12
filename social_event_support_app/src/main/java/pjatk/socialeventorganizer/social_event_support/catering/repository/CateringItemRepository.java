package pjatk.socialeventorganizer.social_event_support.catering.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;

import java.util.List;

@Repository
public interface CateringItemRepository extends JpaRepository<CateringItem, Long> {

    @Query("SELECT ci from catering_item ci WHERE ci.name " +
            "LIKE %:keyword% or ci.itemType " +
            "LIKE %:keyword% OR ci.description " +
            "LIKE %:keyword%")
    Page<CateringItem> findAllWithKeyword(Pageable paging, @Param("keyword") String keyword);

    List<CateringItem> findAllByCatering_Id(long id);
}
