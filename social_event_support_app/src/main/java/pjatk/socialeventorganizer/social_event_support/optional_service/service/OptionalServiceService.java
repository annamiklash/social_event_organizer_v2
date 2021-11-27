package pjatk.socialeventorganizer.social_event_support.optional_service.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.business.model.Business;
import pjatk.socialeventorganizer.social_event_support.business.service.BusinessService;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.model.OptionalServiceBusinessHours;
import pjatk.socialeventorganizer.social_event_support.businesshours.service.service.OptionalServiceBusinessService;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.enums.BusinessVerificationStatusEnum;
import pjatk.socialeventorganizer.social_event_support.exceptions.BusinessVerificationException;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.optional_service.mapper.OptionalServiceMapper;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.dto.OptionalServiceDto;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.Interpreter;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.translator.translation.service.TranslationLanguageService;
import pjatk.socialeventorganizer.social_event_support.optional_service.repository.OptionalServiceRepository;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pjatk.socialeventorganizer.social_event_support.optional_service.enums.OptionalServiceTypeEnum.INTERPRETER;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceService {

    private OptionalServiceRepository optionalServiceRepository;

    private BusinessService businessService;

    private SecurityService securityService;

    private OptionalServiceBusinessService optionalServiceBusinessService;

    private TranslationLanguageService translationLanguageService;

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
        final Optional<OptionalService> optionalService = optionalServiceRepository.findById(id);

        if (optionalService.isPresent()) {
            return optionalService.get();
        }

        throw new NotFoundException("Service with id " + id + " DOES NOT EXIST");
    }

    @Transactional
    public OptionalService create(OptionalServiceDto dto) {
        final UserCredentials userCredentials = securityService.getUserCredentials();
        final Business business = businessService.get(userCredentials.getUserId());

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

    //TODO: FINISH
    public void delete(long id) {
        //cannot delete when there are reservations pending

        //delete images
        //set deletedAt


    }
}
