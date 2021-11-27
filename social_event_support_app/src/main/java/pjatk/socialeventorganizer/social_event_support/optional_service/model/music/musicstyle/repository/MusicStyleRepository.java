package pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.MusicStyle;


@Repository
public interface MusicStyleRepository extends JpaRepository<MusicStyle, Long> {

}
