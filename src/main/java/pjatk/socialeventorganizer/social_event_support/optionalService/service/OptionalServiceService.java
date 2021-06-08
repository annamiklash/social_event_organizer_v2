package pjatk.socialeventorganizer.social_event_support.optionalService.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.address.model.dto.Address;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItem;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.optionalService.mapper.OptionalServiceMapper;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.dto.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.request.OptionalServiceRequest;
import pjatk.socialeventorganizer.social_event_support.optionalService.model.response.OptionalServiceInformationResponse;
import pjatk.socialeventorganizer.social_event_support.optionalService.repository.OptionalServiceRepository;
import pjatk.socialeventorganizer.social_event_support.security.model.UserCredentials;
import pjatk.socialeventorganizer.social_event_support.security.service.SecurityService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OptionalServiceService {

    OptionalServiceRepository optionalServiceRepository;

    OptionalServiceMapper optionalServiceMapper;

    SecurityService securityService;


    public ImmutableList<OptionalServiceInformationResponse> findAll() {
        final List<OptionalService> optionalServiceList = optionalServiceRepository.findAll();

        return optionalServiceList.stream()
                .map(optionalService -> optionalServiceMapper.mapDTOtoInformationResponse(optionalService))
                .collect(ImmutableList.toImmutableList());
    }



    public ImmutableList<OptionalServiceInformationResponse> findByAlias(String alias) {
        final List<OptionalService> optionalServiceList = optionalServiceRepository.findByAliasContaining(alias);
        if (optionalServiceList != null && !optionalServiceList.isEmpty()) {
            return optionalServiceList.stream()
                    .map(optionalService -> optionalServiceMapper.mapDTOtoInformationResponse(optionalService))
                    .collect(ImmutableList.toImmutableList());
        } else {
            throw new NotFoundException("No optionalServices with the alias " + alias + " was found");
        }
    }

    @Transactional
    public void addNewOptionalService(OptionalServiceRequest optionalServiceRequest) {
        final UserCredentials credentials = securityService.getUserCredentials();
        final OptionalService optionalService = optionalServiceMapper.mapToDTO(optionalServiceRequest);
        long businessId= credentials.getUserId();

        optionalService.setBusinessId((int) businessId);


        final OptionalService saved = saveOptionalService(optionalService);
        optionalServiceMapper.mapToResponse(saved);

    }

    public OptionalService saveOptionalService(OptionalService optionalService) {
        log.info("TRYING TO SAVE SERVICE " + optionalService.toString());
        return optionalServiceRepository.save(optionalService);
    }

    public OptionalService findById(Long id) {
        final Optional<OptionalService> optionalOptionalService = optionalServiceRepository.findById(id);
        if (optionalOptionalService.isPresent()) {
           return optionalOptionalService.get();
        }
        throw new NotFoundException("No optional Service with id " + id);
    }
}
