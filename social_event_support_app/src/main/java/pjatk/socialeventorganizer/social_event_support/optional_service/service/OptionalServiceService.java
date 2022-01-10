package pjatk.socialeventorganizer.social_event_support.optional_service.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.Address;
import pjatk.socialeventorganizer.social_event_support.address.service.AddressService;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.model.OptionalServiceAvailability;
import pjatk.socialeventorganizer.social_event_support.availability.optionalservice.repository.OptionalServiceAvailabilityRepository;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.repository.BusinessRepository;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.service.OptionalServiceBusinessHoursService;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.helper.TimestampHelper;
import pjatk.socialeventorganizer.social_event_support.common.mapper.PageableMapper;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.common.util.CollectionUtil;
import pjatk.socialeventorganizer.social_event_support.common.util.DateTimeUtil;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.ActionNotAllowedException;
import pjatk.socialeventorganizer.social_event_support.exceptions.BusinessVerificationException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.image.repository.OptionalServiceImageRepository;
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
import pjatk.socialeventorganizer.social_event_support.reviews.service.service.OptionalServiceReviewService;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.availability.AvailabilityEnum.AVAILABLE;
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

    private final OptionalServiceBusinessHoursService optionalServiceBusinessService;

    private final TranslationLanguageService translationLanguageService;

    private final OptionalServiceAvailabilityRepository optionalServiceAvailabilityRepository;

    private final OptionalServiceForChosenLocationRepository optionalServiceForChosenLocationRepository;

    private final OptionalServiceReviewService optionalServiceReviewService;

    private final OptionalServiceImageRepository optionalServiceImageRepository;

    private final TimestampHelper timestampHelper;

    public ImmutableList<OptionalService> list(CustomPage customPage, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageableMapper.map(customPage);
        final Page<OptionalService> page = optionalServiceRepository.findAllWithKeyword(paging, keyword);

        final List<OptionalService> result = page.get().collect(Collectors.toList());
        for (OptionalService optionalService : result) {
            if (optionalService.getType().equals(INTERPRETER.getValue())) {
                ((Interpreter) optionalService).setLanguages(
                        new HashSet<>(translationLanguageService.getAllByInterpreterId(optionalService.getId()))
                );
            }
        }
        return ImmutableList.copyOf(page.get()
                .peek(location -> location.setRating(optionalServiceReviewService.getRating(location.getId())))
                .collect(Collectors.toList()));
    }

    public OptionalService get(long id) {
        return optionalServiceRepository.findWithImages(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " DOES NOT EXIST"));
    }

    public List<String> getCities() {
        return optionalServiceRepository.findDistinctCities();
    }


    public OptionalService getWithDetail(long serviceId) {
        final OptionalService optionalService = optionalServiceRepository.findWithDetail(serviceId)
                .orElseThrow(() -> new NotFoundException("Service with serviceId " + serviceId + " DOES NOT EXIST"));

        optionalService.setRating(optionalServiceReviewService.getRating(serviceId));
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

        List<MusicStyle> musicStyles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dto.getMusicStyle())) {
            musicStyles = dto.getMusicStyle().stream()
                    .map(musicStyleDto -> musicStyleService.getByName(musicStyleDto.getName()))
                    .collect(Collectors.toList());
        }

        List<TranslationLanguage> translationLanguages = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dto.getTranslationLanguages())) {
            translationLanguages = dto.getTranslationLanguages().stream()
                    .map(language -> translationLanguageService.getByName(language.getName()))
                    .collect(Collectors.toList());
        }

        final OptionalService optionalService = OptionalServiceMapper.fromDto(dto, musicStyles, translationLanguages);

        final Address address = addressService.create(dto.getAddress());

        final List<OptionalServiceBusinessHours> businessHours = optionalServiceBusinessService.create(dto.getBusinessHours());

        optionalService.setServiceAddress(address);
        optionalService.setBusiness(business);
        optionalService.setOptionalServiceBusinessHours(new HashSet<>(businessHours));
        optionalService.setRating(0.0);
        optionalService.setCreatedAt(timestampHelper.now());
        optionalService.setModifiedAt(timestampHelper.now());

        optionalServiceRepository.save(optionalService);

        createAvailabilitiesForNewService(businessHours, optionalService);

        return optionalService;
    }

    public OptionalService edit(OptionalServiceDto dto, long id) {
        final OptionalService optionalService = get(id);

        optionalService.setAlias(dto.getAlias());
        optionalService.setDescription(dto.getDescription());
        optionalService.setType(optionalService.getType());
        optionalService.setModifiedAt(timestampHelper.now());

        optionalServiceRepository.save(optionalService);

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
        optionalServices.forEach(optionalService -> optionalService.setRating(optionalServiceReviewService.getRating(optionalService.getId())));

        return ImmutableList.copyOf(optionalServices);
    }

    public boolean isAvailable(long serviceId, String date, String timeFrom, String timeTo) {
        String from = DateTimeUtil.joinDateAndTime(date, timeFrom);
        String to = DateTimeUtil.joinDateAndTime(date, timeTo);
        return optionalServiceRepository.available(serviceId, date, from, to).isPresent();
    }

    public OptionalService getWithImages(long id) {
        return optionalServiceRepository.findWithImages(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " DOES NOT EXIST"));

    }

    public Long count(String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();
        return optionalServiceRepository.countAll(keyword);
    }


    public ImmutableList<OptionalService> getByBusinessId(long id) {
        return ImmutableList.copyOf(optionalServiceRepository.findAllByBusiness_Id(id));
    }


    @Transactional(rollbackOn = Exception.class)
    public void delete(long id) {
        final OptionalService serviceToDelete = optionalServiceRepository.getAllServiceInformation(id)
                .orElseThrow(() -> new NotFoundException("Service with id " + id + " DOES NOT EXIST"));

        boolean hasPendingReservations = hasPendingReservations(serviceToDelete);
        if (hasPendingReservations) {
            throw new ActionNotAllowedException("Cannot delete service with reservations pending");
        }

        CollectionUtil.emptyListIfNull(serviceToDelete.getOptionalServiceBusinessHours())
                .forEach(optionalServiceBusinessService::delete);

        CollectionUtil.emptyListIfNull(serviceToDelete.getAvailability())
                .forEach(optionalServiceAvailabilityRepository::delete);

        CollectionUtil.emptyListIfNull(serviceToDelete.getServiceForLocation())
                .forEach(optionalServiceForChosenLocationRepository::delete);

        CollectionUtil.emptyListIfNull(serviceToDelete.getImages())
                .forEach(optionalServiceImageRepository::delete);

        addressService.delete(serviceToDelete.getServiceAddress());

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

        optionalServiceRepository.delete(serviceToDelete);
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

    private void createAvailabilitiesForNewService(List<OptionalServiceBusinessHours> businessHours, OptionalService service) {
        for (OptionalServiceBusinessHours businessHour : businessHours) {
            final String day = businessHour.getDay();
            final DayOfWeek dayOfWeek = DayOfWeek.valueOf(day);
            LocalDate startDate = LocalDate.now();
            final LocalDate endDate = startDate.plusDays(30);

            while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
                final LocalDate nextWeekDay = startDate.with(TemporalAdjusters.next(dayOfWeek));
                final OptionalServiceAvailability availability = OptionalServiceAvailability.builder()
                        .date(nextWeekDay)
                        .timeFrom(LocalDateTime.of(nextWeekDay, businessHour.getTimeFrom()))
                        .timeTo(LocalDateTime.of(nextWeekDay, businessHour.getTimeTo()))
                        .status(AVAILABLE.name())
                        .build();

                availability.setOptionalService(service);
                optionalServiceAvailabilityRepository.save(availability);
                startDate = nextWeekDay;
            }
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
