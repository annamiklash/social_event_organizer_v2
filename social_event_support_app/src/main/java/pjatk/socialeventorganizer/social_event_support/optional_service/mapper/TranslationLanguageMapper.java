package pjatk.socialeventorganizer.social_event_support.optional_service.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.TranslationLanguageDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.translation.model.TranslationLanguage;

@UtilityClass
public class TranslationLanguageMapper {

    public TranslationLanguageDto toDto(TranslationLanguage translationLanguage) {
        return TranslationLanguageDto.builder()
                .id(translationLanguage.getId())
                .name(translationLanguage.getName())
                .build();
    }

    public TranslationLanguage fromDto(TranslationLanguageDto dto) {
        return TranslationLanguage.builder()
                .name(dto.getName())
                .build();
    }
}
