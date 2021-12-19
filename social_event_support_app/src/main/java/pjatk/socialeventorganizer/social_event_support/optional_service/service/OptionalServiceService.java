package pjatk.socialeventorganizer.social_event_support.optional_service.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.service.OptionalServiceBusinessService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.BusinessVerificationException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.optional_service.mapper.OptionalServiceMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.FilterOptionalServiceDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.Interpreter;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.translation.model.TranslationLanguage;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.interpreter.translation.service.TranslationLanguageService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.MusicStyle;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.repostory.OptionalServiceForChosenLocationRepository;
import pjatk.socialeventorganizer.social_event_support.optional_service.repository.OptionalServiceRepository;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.optional_service.enums.OptionalServiceTypeEnum.INTERPRETER;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceService {

    private final OptionalServiceRepository optionalServiceRepository;

    private final BusinessRepository businessRepository;

    private final SecurityService securityService;

    private final OptionalServiceBusinessService optionalServiceBusinessService;

    private final TranslationLanguageService translationLanguageService;

    private final OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository;

    private final OptionalServiceForChosenLocationRepository optionalServiceForChosenLocationRepository;

    public ImmutableList<OptionalService> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPage.getFirstResult(), customPage.getMaxResult(), Sort.by(customPage.getSort()).descending());
        final Page<OptionalService> page = optionalServiceRepository.findAllWithKeyword(paging, keyword);

        final List<OptionalService> result = page.get().collect(Collectors.toList());
        for (OptionalService optionalService : result) {
            if (optionalService.getType().equals(INTERPRETER.getValue())) {
                ((Interpreter) optionalService).setLanguages(
                        new HashSet<>(translationLanguageService.getAllByInterpreterId(optionalService.getId()))
                );
            }
        }
        return ImmutableList.copyOf(result);
    }

    public OptionalService get(long id) {
        return optionalServiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " DOES NOT EXIST"));
    }

    public OptionalService getWithDetail(long id) {
        final OptionalService optionalService = optionalServiceRepository.findWithDetail(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " DOES NOT EXIST"));

        final String type = optionalService.getType();
        switch (type) {
            case "INTERPRETER":
                final ImmutableSet<TranslationLanguage> languages = ImmutableSet.copyOf(((Interpreter) optionalService).getLanguages());
                ((Interpreter) optionalService).setLanguages(languages);
                break;

            case "MUSICIAN":
            case "MUSIC BAND":
            case "SINGER":
            case "DJ":
                final ImmutableSet<MusicStyle> musicStyles = ImmutableSet.copyOf(optionalService.getStyles());
                optionalService.setStyles(musicStyles);
                break;
            default:
                break;
        }
        return optionalService;
    }

    @Transactional(rollbackOn = Exception.class)
    public OptionalService create(OptionalServiceDto dto) {
        final UserCredentials userCredentials = securityService.getUserCredentials();

        final Business business = businessRepository.findById(userCredentials.getUserId())
                .orElseThrow(() -> new NotFoundException("Business with id " + userCredentials.getUserId() + " DOES NOT EXIST"));

        if (!business.getVerificationStatus().equals(String.valueOf(BusinessVerificationStatusEnum.VERIFIED))) {
            throw new BusinessVerificationException(BusinessVerificationException.Enum.BUSINESS_NOT_VERIFIED);
        }

        final List<OptionalServiceBusinessHours> businessHours = optionalServiceBusinessService.create(dto.getBusinessHours());

        final OptionalService optionalService = OptionalServiceMapper.fromDto(dto);

        optionalService.setBusiness(business);
        optionalService.setOptionalServiceBusinessHours(new HashSet<>(businessHours));
        optionalService.setCreatedAt(LocalDateTime.now());
        optionalService.setModifiedAt(LocalDateTime.now());

        save(optionalService);

        return optionalService;
    }

    public void save(OptionalService optionalService) {
        optionalServiceRepository.save(optionalService);
    }

    public OptionalService edit(OptionalServiceDto dto, long id) {
        final OptionalService optionalService = get(id);

        optionalService.setAlias(dto.getAlias());
        optionalService.setDescription(dto.getDescription());
        optionalService.setType(optionalService.getType());
        optionalService.setModifiedAt(LocalDateTime.now());

        save(optionalService);

        return optionalService;
    }

    public ImmutableList<OptionalService> search(FilterOptionalServiceDto dto) {
        String keyword = dto.getKeyword();
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        List<OptionalService> optionalServices;
        if (dto.getDate() != null && dto.getTimeFrom() != null && dto.getTimeTo() != null) {
            optionalServices = optionalServiceRepository.search(dto.getDate(), dto.getTimeFrom(), dto.getTimeTo(), dto.getType());
        }
        if (dto.getDate() != null && (dto.getTimeFrom() == null || dto.getTimeTo() == null)) {
            optionalServices = optionalServiceRepository.searchWithoutDateTime(dto.getType());

        }
        //TODO: FINISH
        return null;
    }

    public boolean isAvailable(long serviceId, String date, String timeFrom, String timeTo) {
        return optionalServiceRepository.available(serviceId, date, timeFrom, timeTo).isPresent();

    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteLogical(long id) {
        final OptionalService serviceToDelete = optionalServiceRepository.getAllServiceInformation(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " DOES NOT EXIST"));

        boolean hasPendingReservations = hasPendingReservations(serviceToDelete);
        if (hasPendingReservations) {
            throw new ActionNotAllowedException("Cannot delete service with reservations pending");
        }

        ImmutableSet.copyOf(serviceToDelete.getOptionalServiceBusinessHours())
                .forEach(optionalServiceBusinessService::delete);

        ImmutableSet.copyOf(serviceToDelete.getAvailability())
                .forEach(optionalServiceAvailabilityRepository::delete);

        ImmutableSet.copyOf(serviceToDelete.getServiceForLocation())
                .forEach(optionalServiceForChosenLocationRepository::delete);

        final String type = serviceToDelete.getType();
        switch (type) {
            case "INTERPRETER":
                final ImmutableSet<TranslationLanguage> languages = ImmutableSet.copyOf(((Interpreter) serviceToDelete).getLanguages());
                for (TranslationLanguage language : languages) {
                    ((Interpreter) serviceToDelete).removeLanguage(language);
                }
                break;

            case "MUSICIAN":
            case "MUSIC BAND":
            case "SINGER":
            case "DJ":
                final ImmutableSet<MusicStyle> musicStyles = ImmutableSet.copyOf(serviceToDelete.getStyles());
                for (MusicStyle musicStyle : musicStyles) {
                    serviceToDelete.removeMusicStyle(musicStyle);
                }
                break;
            default:
                break;
        }
        serviceToDelete.setModifiedAt(LocalDateTime.now());
        serviceToDelete.setDeletedAt(LocalDateTime.now());
    }

    private boolean hasPendingReservations(OptionalService serviceToDelete) {
        return serviceToDelete.getServiceForLocation().stream()
                .map(optionalService -> optionalService.getLocationForEvent().getEvent())
                .anyMatch(organizedEvent -> organizedEvent.getDate().isAfter(LocalDate.now()));
    }

}
