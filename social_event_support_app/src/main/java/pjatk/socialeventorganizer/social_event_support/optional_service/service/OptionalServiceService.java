package pjatk.socialeventorganizer.social_event_support.optional_service.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.service.OptionalServiceBusinessService;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
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
import pjatk.socialeventorganizer.social_event_support.optional_service.model.kidperformer.KidsPerformer;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.MusicBand;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.MusicStyle;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.music.musicstyle.service.MusicStyleService;
import pjatk.socialeventorganizer.social_event_support.optional_service.optional_service_for_location.repostory.OptionalServiceForChosenLocationRepository;
import pjatk.socialeventorganizer.social_event_support.optional_service.repository.OptionalServiceRepository;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.optional_service.enums.OptionalServiceTypeEnum.INTERPRETER;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceService {

    private final OptionalServiceRepository optionalServiceRepository;

    private final BusinessRepository businessRepository;

    private final SecurityService securityService;

    private final AddressService addressService;

    private final MusicStyleService musicStyleService;

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
        final Address address = addressService.create(dto.getAddress());

        final List<OptionalServiceBusinessHours> businessHours = optionalServiceBusinessService.create(dto.getBusinessHours());

        final OptionalService optionalService = OptionalServiceMapper.fromDto(dto);

        optionalService.setServiceAddress(address);
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
        String city = dto.getCity();
        city = Strings.isNullOrEmpty(city) ? "" : city.substring(0, dto.getCity().indexOf(','));

        List<OptionalService> optionalServices;

        if (dto.getDate() != null && dto.getType() != null) {
            optionalServices = optionalServiceRepository.search(dto.getDate(), dto.getType(), city);
            optionalServices = filterByType(optionalServices, dto);

        } else if (dto.getType() != null && dto.getDate() == null) {
            optionalServices = optionalServiceRepository.searchByType(dto.getType(), city);
            optionalServices = filterByType(optionalServices, dto);

        } else if (dto.getDate() != null && dto.getType() == null) {
            optionalServices = optionalServiceRepository.searchByDate(dto.getDate(), city);

        } else {
            optionalServices = optionalServiceRepository.getAll();
        }

        optionalServices = filterByPrice(dto.getMinPrice(), dto.getMaxPrice(), optionalServices);

        return ImmutableList.copyOf(optionalServices);
    }


    public boolean isAvailable(long serviceId, String date, String timeFrom, String timeTo) {
        return optionalServiceRepository.available(serviceId, date, timeFrom, timeTo).isPresent();

    }
     public OptionalService getWithImages(long id){
        return optionalServiceRepository.findWithImages(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " DOES NOT EXIST"));

     }


    public ImmutableList<OptionalService> getByBusinessId(long id) {
        return ImmutableList.copyOf(optionalServiceRepository.findAllByBusiness_Id(id));
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

    private List<OptionalService> filterByPrice(String minPrice, String maxPrice, List<OptionalService> services) {
        if (minPrice == null && maxPrice == null) {
            return services;
        } else if (minPrice != null && maxPrice == null) {
            return services.stream()
                    .filter(service -> Converter.convertPriceString(minPrice).compareTo(service.getServiceCost()) < 0 ||
                            Converter.convertPriceString(minPrice).compareTo(service.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        } else if (minPrice == null) {
            return services.stream()
                    .filter(service -> Converter.convertPriceString(maxPrice).compareTo(service.getServiceCost()) > 0 ||
                            Converter.convertPriceString(maxPrice).compareTo(service.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        } else {
            return services.stream()
                    .filter(service -> Converter.convertPriceString(minPrice).compareTo(service.getServiceCost()) < 0 ||
                            Converter.convertPriceString(minPrice).compareTo(service.getServiceCost()) == 0)
                    .filter(service -> Converter.convertPriceString(maxPrice).compareTo(service.getServiceCost()) > 0 ||
                            Converter.convertPriceString(maxPrice).compareTo(service.getServiceCost()) == 0)
                    .collect(Collectors.toList());
        }
    }


    private List<OptionalService> filterByType(List<OptionalService> optionalServices, FilterOptionalServiceDto dto) {

        if (dto.getMusicStyles() != null && dto.getBandPeopleCount() != null) {
            final Set<MusicStyle> musicStyles = dto.getMusicStyles()
                    .stream()
                    .map(musicStyleService::getByName)
                    .collect(Collectors.toSet());

            return optionalServices.stream()
                    .filter(optionalService -> CollectionUtils.containsAny(musicStyles, optionalService.getStyles()) &&
                            ((MusicBand) optionalService).getPeopleCount() == dto.getBandPeopleCount())
                    .collect(Collectors.toList());
        }

        if (dto.getMusicStyles() != null && dto.getBandPeopleCount() == null) {
            final Set<MusicStyle> musicStyles = dto.getMusicStyles()
                    .stream()
                    .map(musicStyleService::getByName)
                    .collect(Collectors.toSet());

            return optionalServices.stream()
                    .filter(optionalService -> CollectionUtils.containsAny(musicStyles, optionalService.getStyles()))
                    .collect(Collectors.toList());
        }

        if (dto.getBandPeopleCount() != null && dto.getMusicStyles() == null) {
            return optionalServices.stream()
                    .filter(optionalService -> ((MusicBand) optionalService).getPeopleCount() == dto.getBandPeopleCount())
                    .collect(Collectors.toList());
        }

        if (dto.getAgeFrom() != null && dto.getAgeTo() != null) {
            return optionalServices.stream()
                    .filter(optionalService -> ((KidsPerformer) optionalService).getAgeFrom() >= dto.getAgeFrom() &&
                            ((KidsPerformer) optionalService).getAgeTo() <= dto.getAgeTo())
                    .collect(Collectors.toList());
        }
        if (dto.getAgeFrom() != null && dto.getAgeTo() == null) {
            return optionalServices.stream()
                    .filter(optionalService -> ((KidsPerformer) optionalService).getAgeFrom() >= dto.getAgeFrom())
                    .collect(Collectors.toList());
        }

        if (dto.getAgeFrom() == null && dto.getAgeTo() != null) {
            return optionalServices.stream()
                    .filter(optionalService -> ((KidsPerformer) optionalService).getAgeTo() <= dto.getAgeTo())
                    .collect(Collectors.toList());
        }

        if (dto.getLanguages() != null) {
            final Set<TranslationLanguage> languages = dto.getLanguages().stream()
                    .map(translationLanguageService::getByName)
                    .collect(Collectors.toSet());
            return optionalServices.stream()
                    .filter(optionalService -> CollectionUtils.containsAny(languages, ((Interpreter) optionalService).getLanguages()))
                    .collect(Collectors.toList());
        }
        return optionalServices;
    }
}
