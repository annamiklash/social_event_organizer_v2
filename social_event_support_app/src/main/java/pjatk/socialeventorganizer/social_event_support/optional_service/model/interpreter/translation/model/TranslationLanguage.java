package pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.translation.model;

import lombok.*;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.Interpreter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@EqualsAndHashCode(exclude = "interpreters")
@AllArgsConstructor
@RequiredArgsConstructor
@Entity(name = "translation_language")
public class TranslationLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_language")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "languages", fetch = FetchType.LAZY)
    private Set<Interpreter> interpreters = new HashSet<>();

}
