package pjatk.socialeventorganizer.social_event_support.optional_service.model.translator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.translation.model.TranslationLanguage;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@DiscriminatorValue("INTERPRETER")
public class Interpreter extends OptionalService {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "service_translator_language",
            joinColumns = @JoinColumn(name = "id_optional_service"),
            inverseJoinColumns = @JoinColumn(name = "id_language"))
    private Set<TranslationLanguage> languages;

}
