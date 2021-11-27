package pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.translation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.translation.model.TranslationLanguage;

import java.util.List;

@Repository
public interface TranslationLanguageRepository extends JpaRepository<TranslationLanguage, Long> {

    @Query("SELECT l from translation_language l " +
            "left join fetch l.interpreters i " +
            "where i.id = :id ")
    List<TranslationLanguage> getByInterpreterId(@Param("id") long id);
}
