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

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CateringOrderChoiceService {

    private final CateringOrderChoiceRepository cateringOrderChoiceRepository;
    private final CateringForChosenEventLocationService cateringForChosenEventLocationService;
    private final CateringItemService cateringItemService;


    public ImmutableList<CateringOrderChoice> getAll(long cateringId, long reservationId) {
        return ImmutableList.copyOf(cateringOrderChoiceRepository.getAll(cateringId, reservationId));
    }

    public ImmutableList<CateringOrderChoice> getAll(long cateringId) {
        return ImmutableList.copyOf(cateringOrderChoiceRepository.getAll(cateringId));
    }

    public List<CateringOrderChoice> create(CateringOrderChoiceDto[] dtos, long reservationId) {

        final CateringForChosenEventLocation catering = cateringForChosenEventLocationService.get(reservationId);
        final List<CateringOrderChoice> result = new ArrayList<>();

        for (CateringOrderChoiceDto dto : dtos) {
            final CateringItem cateringItem = cateringItemService.get(dto.getItemId());
            final CateringOrderChoice orderChoice = CateringOrderChoiceMapper.fromDto(dto);
            orderChoice.setItem(cateringItem);
            orderChoice.setEventLocationCatering(catering);

            result.add(orderChoice);

        }
        cateringOrderChoiceRepository.saveAll(result);
        return result;
    }

    public CateringOrderChoice create(CateringOrderChoiceDto dto, long itemId, long reservationId) {
        final CateringForChosenEventLocation catering = cateringForChosenEventLocationService.get(reservationId);
        final CateringItem cateringItem = cateringItemService.get(itemId);

        final CateringOrderChoice orderChoice = CateringOrderChoiceMapper.fromDto(dto);
        orderChoice.setEventLocationCatering(catering);
        orderChoice.setItem(cateringItem);

        cateringOrderChoiceRepository.save(orderChoice);

        return orderChoice;
    }

    public CateringOrderChoice edit(CateringOrderChoiceDto dto, long orderChoiceId) {
        final CateringOrderChoice orderChoice = cateringOrderChoiceRepository.findWithDetail(orderChoiceId)
                .orElseThrow(() -> new NotFoundException("No caatering order with id " + orderChoiceId));
        orderChoice.setAmount(dto.getAmount());

        cateringOrderChoiceRepository.save(orderChoice);
        return orderChoice;
    }

    public void delete(long id) {
        final CateringOrderChoice orderChoice = cateringOrderChoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No caatering order with id " + id));

        cateringOrderChoiceRepository.delete(orderChoice);
    }


}


