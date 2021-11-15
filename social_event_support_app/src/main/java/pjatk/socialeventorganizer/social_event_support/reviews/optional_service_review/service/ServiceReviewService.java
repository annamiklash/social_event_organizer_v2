package pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer;
import pjatk.socialeventorganizer.social_event_support.customer.service.CustomerService;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;
import pjatk.socialeventorganizer.social_event_support.optional_service.model.OptionalService;
import pjatk.socialeventorganizer.social_event_support.optional_service.service.OptionalServiceService;
import pjatk.socialeventorganizer.social_event_support.reviews.mapper.ReviewMapper;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.OptionalServiceReview;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.model.dto.ServiceReviewDto;
import pjatk.socialeventorganizer.social_event_support.reviews.optional_service_review.repository.ServiceReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceReviewService {

    private final ServiceReviewRepository serviceReviewRepository;

    private final CustomerService customerService;

    private final OptionalServiceService optionalServiceService;

    public void save(OptionalServiceReview optionalServiceReview) {
        serviceReviewRepository.save(optionalServiceReview);
    }

    public OptionalServiceReview leaveServiceReview(long id, long serviceId, ServiceReviewDto dto) {
        final Customer customer = customerService.get(id);

        final OptionalService optionalService = optionalServiceService.get(serviceId);

        final OptionalServiceReview optionalServiceReview = ReviewMapper.fromServiceReviewDto(dto);
        optionalServiceReview.setOptionalService(optionalService);
        optionalServiceReview.setCustomer(customer);
        optionalServiceReview.setCreatedAt(LocalDateTime.now());

        save(optionalServiceReview);

        return optionalServiceReview;
    }

    public List<OptionalServiceReview> getByServiceId(long id) {
        if (exists(id)) {
            return serviceReviewRepository.getByServiceId(id);
        }
        throw new NotFoundException("Catering with id " + id + " does not exist");

    }

    public boolean exists(long id) {
        return serviceReviewRepository.existsByOptionalService_Id(id);
    }
}
