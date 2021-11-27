package pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.translation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.translation.model.TranslationLanguage;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.translation.repository.TranslationLanguageRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TranslationLanguageService {

    private final TranslationLanguageRepository translationLanguageRepository;

    public List<TranslationLanguage> getAllByInterpreterId(long id) {
        return translationLanguageRepository.getByInterpreterId(id);
    }
}
