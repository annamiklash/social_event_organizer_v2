package pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;

import javax.persistence.*;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "music_style")
public class MusicStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_music_style")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "styles", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<OptionalService> optionalServices;

}
