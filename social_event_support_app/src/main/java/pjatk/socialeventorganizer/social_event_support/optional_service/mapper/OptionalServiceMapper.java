package pjatk.socialeventorganizer.social_event_support.optional_service.mapper;

import lombok.experimental.UtilityClass;
import pjatk.socialeventorganizer.social_event_support.availability.mapper.AvailabilityMapper;
import pjatk.socialeventorganizer.social_event_support.businesshours.mapper.BusinessHoursMapper;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.optional_service.enums.OptionalServiceTypeEnum;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.Interpreter;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.kidperformer.KidsPerformer;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.MusicBand;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.Musician;
import pjatk.socialeventorganizer.social_event_support.optional_service.validator.Validator;

import java.util.stream.Collectors;

@UtilityClass
public class OptionalServiceMapper {

    public OptionalServiceDto toDto(OptionalService optionalService) {
        final OptionalServiceDto dto = OptionalServiceDto.builder()
                .id(optionalService.getId())
                .type(optionalService.getType())
                .alias(optionalService.getAlias())
                .firstName(optionalService.getFirstName())
                .lastName(optionalService.getLastName())
                .description(optionalService.getDescription())
                .serviceCost(String.valueOf(optionalService.getServiceCost()))
                .email(optionalService.getEmail())
                .createdAt(DateTimeUtil.fromLocalDateTimetoString(optionalService.getCreatedAt()))
                .modifiedAt(DateTimeUtil.fromLocalDateTimetoString(optionalService.getModifiedAt()))
                .deletedAt(DateTimeUtil.fromLocalDateTimetoString(optionalService.getDeletedAt()))
                .build();

        final OptionalServiceTypeEnum type = OptionalServiceTypeEnum.valueOfLabel(optionalService.getType());

        switch (type) {
            case KIDS_PERFORMER:
                dto.setKidAgeFrom(((KidsPerformer) optionalService).getAgeFrom());
                dto.setKidAgeTo(((KidsPerformer) optionalService).getAgeFrom());
                dto.setKidPerformerType(dto.getKidPerformerType());
                return dto;

            case INTERPRETER:
                dto.setTranslationLanguages(((Interpreter) optionalService).getLanguages()
                        .stream()
                        .map(TranslationLanguageMapper::toDto)
                        .collect(Collectors.toSet()));
                return dto;

            case MUSIC_BAND:
                dto.setMusicBandPeopleCount(((MusicBand) optionalService).getPeopleCount());
                dto.setMusicStyle(optionalService.getStyles()
                        .stream()
                        .map(MusicStyleMapper::toDto)
                        .collect(Collectors.toSet()));
                return dto;

            case MUSICIAN:
                dto.setMusicStyle(optionalService.getStyles()
                        .stream()
                        .map(MusicStyleMapper::toDto)
                        .collect(Collectors.toSet()));
                dto.setInstrument(((Musician) optionalService).getInstrument());
                return dto;

            case DJ:
            case SINGER:
                dto.setMusicStyle(optionalService.getStyles()
                        .stream()
                        .map(MusicStyleMapper::toDto)
                        .collect(Collectors.toSet()));
                return dto;

            case HOST:
            case OTHER:
            default:
                return dto;
        }
    }

    public OptionalService fromDto(OptionalServiceDto dto) {
        OptionalService optionalService = OptionalService.builder()
                .type(dto.getType())
                .alias(dto.getAlias())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .description(dto.getDescription())
                .serviceCost(Converter.convertPriceString(dto.getServiceCost()))
                .email(dto.getEmail())
                .build();

        final OptionalServiceTypeEnum type = OptionalServiceTypeEnum.valueOfLabel(dto.getType());

        Validator.validateType(dto);

        switch (type) {
            case KIDS_PERFORMER:
                ((KidsPerformer) optionalService).setAgeFrom(dto.getKidAgeFrom());
                ((KidsPerformer) optionalService).setAgeTo(dto.getKidAgeTo());
                ((KidsPerformer) optionalService).setKidsPerformerType(dto.getKidPerformerType());
                return optionalService;

            case INTERPRETER:
                ((Interpreter) optionalService).setLanguages(
                        dto.getTranslationLanguages().stream()
                                .map(TranslationLanguageMapper::fromDto)
                                .collect(Collectors.toSet()));
                return optionalService;

            case MUSIC_BAND:
                ((MusicBand) optionalService).setPeopleCount(dto.getMusicBandPeopleCount());
                optionalService.setStyles(
                        dto.getMusicStyle().stream()
                                .map(MusicStyleMapper::fromDto)
                                .collect(Collectors.toSet()));
                return optionalService;

            case MUSICIAN:
                optionalService.setStyles(
                        dto.getMusicStyle().stream()
                                .map(MusicStyleMapper::fromDto)
                                .collect(Collectors.toSet()));
                ((Musician) optionalService).setInstrument(dto.getInstrument());
                return optionalService;

            case DJ:
            case SINGER:
                optionalService.setStyles(
                        dto.getMusicStyle().stream()
                                .map(MusicStyleMapper::fromDto)
                                .collect(Collectors.toSet()));
                return optionalService;

            case HOST:
            case OTHER:
            default:
                return optionalService;
        }
    }

    public OptionalServiceDto toDtoWithDetails(OptionalService optionalService) {
        final OptionalServiceDto dto = toDto(optionalService);
//        dto.setBusiness(BusinessMapper.toDto(optionalService.getBusiness()));

        dto.setBusinessHours(optionalService.getOptionalServiceBusinessHours().stream()
                .map(BusinessHoursMapper::toDto)
                .collect(Collectors.toList()));

        dto.setServiceAvailability(optionalService.getAvailability().stream()
                .map(AvailabilityMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }
}
