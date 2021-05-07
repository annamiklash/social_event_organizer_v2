package pjatk.socialeventorganizer.social_event_support.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.mapper.CateringItemMapper;
import pjatk.socialeventorganizer.social_event_support.model.dto.CateringItem;
import pjatk.socialeventorganizer.social_event_support.model.request.CateringItemRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.CateringItemResponse;
import pjatk.socialeventorganizer.social_event_support.repository.CateringItemRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CateringItemService {

    CateringItemRepository repository;

    CateringItemMapper mapper;

    public List<CateringItem> findAll() {
        log.info("GET ALL CATERING_ITEM");
        return (List<CateringItem>) repository.findAll();
    }

    public CateringItemResponse addNewCateringItem(CateringItemRequest request) {
        final CateringItem cateringItem = mapper.mapToDTO(request);
        log.info("TRYING TO SAVE " + cateringItem.toString());

        repository.save(cateringItem);
        return mapper.mapToResponse(cateringItem);
    }

    public void updateCateringItem(Long cateringItemId, CateringItemRequest request) {
        final CateringItem cateringItem = mapper.mapToDTO(request, cateringItemId);
        log.info("TRYING TO UPDATE " + cateringItem);
        repository.save(cateringItem);
        log.info("UPDATED");
    }

    public void deleteCateringItem(Long id) {
        log.info("TRYING TO DELETE CATERING WITH ID " + id);
        repository.deleteById(id);
    }
}
