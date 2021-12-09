package pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.service;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;
import pjatk.socialeventorganizer.social_event_support.catering.service.CateringItemService;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.mapper.CateringOrderChoiceMapper;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringForChosenEventLocation;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.CateringOrderChoice;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.model.dto.CateringOrderChoiceDto;
import pjatk.socialeventorganizer.social_event_support.cateringforchosenevent.repository.CateringOrderChoiceRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

@Service
@AllArgsConstructor
@Slf4j
public class CateringOrderChoiceService {

    private final CateringOrderChoiceRepository cateringOrderChoiceRepository;

    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;

    private final CateringItemService cateringItemService;


    public ImmutableList<CateringOrderChoice> getAll(long cateringId) {
        return ImmutableList.copyOf(cateringOrderChoiceRepository.getAll(cateringId));
    }

    public CateringOrderChoice create(CateringOrderChoiceDto dto, long itemId, long cateringId) {
        final CateringForChosenEventLocation catering = cateringForChosenEventLocationService.get(cateringId);
        final CateringItem cateringItem = cateringItemService.get(itemId);

        final CateringOrderChoice orderChoice = CateringOrderChoiceMapper.fromDto(dto);
        orderChoice.setEventLocationCatering(catering);
        orderChoice.setItem(cateringItem);

        save(orderChoice);

        return orderChoice;
    }

    public CateringOrderChoice edit(CateringOrderChoiceDto dto, long orderChoiceId) {
        final CateringOrderChoice orderChoice = getWithDetail(orderChoiceId);
        orderChoice.setAmount(dto.getAmount());

        save(orderChoice);
        return orderChoice;
    }

    public CateringOrderChoice getWithDetail(long orderChoiceId) {
        return cateringOrderChoiceRepository.findWithDetail(orderChoiceId)
                .orElseThrow(() -> new NotFoundException("No caatering order with id " + orderChoiceId));
    }

    public void save(CateringOrderChoice cateringOrderChoice) {
        cateringOrderChoiceRepository.save(cateringOrderChoice);
    }

    public CateringOrderChoice get(long orderChoiceId) {
        return cateringOrderChoiceRepository.findById(orderChoiceId)
                .orElseThrow(() -> new NotFoundException("No caatering order with id " + orderChoiceId));
    }

    public void delete(long id) {
        final CateringOrderChoice orderChoice = get(id);
        cateringOrderChoiceRepository.delete(orderChoice);
    }
}
