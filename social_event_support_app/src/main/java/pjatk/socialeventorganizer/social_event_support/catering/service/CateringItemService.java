package pjatk.socialeventorganizer.social_event_support.catering.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.catering.mapper.CateringItemMapper;
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering;
import pjatk.socialeventorganizer.social_event_support.catering.model.CateringItem;
import pjatk.socialeventorganizer.social_event_support.catering.model.dto.CateringItemDto;
import pjatk.socialeventorganizer.social_event_support.catering.repository.CateringItemRepository;
import pjatk.socialeventorganizer.social_event_support.common.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CateringItemService {

    CateringItemRepository cateringItemRepository;

    CateringService cateringService;

    public ImmutableList<CateringItem> list(CustomPage customPagination, String keyword) {
        keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getFirstResult(), customPagination.getMaxResult(), Sort.by(customPagination.getSortBy()).descending());

        final Page<CateringItem> page = cateringItemRepository.findAllWithKeyword(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }


    public ImmutableList<CateringItem> listAllByCateringId(long cateringId) {
        final List<CateringItem> cateringItemList = cateringItemRepository.findAllByCatering_Id(cateringId);

        return ImmutableList.copyOf(cateringItemList);
    }

    public CateringItem get(long id) {
        final Optional<CateringItem> optionalCateringItem = cateringItemRepository.findById(id);
        if (optionalCateringItem.isPresent()) {
            return optionalCateringItem.get();
        }
        throw new NotFoundException("Catering item with id " + id + " DOES NOT EXIST");

    }


    public CateringItem create(CateringItemDto dto, long cateringId) {
        final Catering catering = cateringService.get(cateringId);

        final CateringItem cateringItem = CateringItemMapper.fromDto(dto);

        cateringItem.setCatering(catering);
        cateringItem.setCreatedAt(LocalDateTime.now());
        cateringItem.setModifiedAt(LocalDateTime.now());

        catering.setModifiedAt(LocalDateTime.now());

        log.info("TRYING TO SAVE " + cateringItem);

        cateringItemRepository.save(cateringItem);

        return cateringItem;
    }

    public CateringItem edit(long cateringItemId, CateringItemDto dto) {

        final CateringItem cateringItem = get(cateringItemId);

        cateringItem.setItemType(dto.getType());
        cateringItem.setName(dto.getName());
        cateringItem.setDescription(dto.getDescription());
        cateringItem.setVegan(dto.isVegan());
        cateringItem.setVegetarian(dto.isVegetarian());
        cateringItem.setGlutenFree(dto.isGlutenFree());
        cateringItem.setServingPrice(Converter.convertPriceString(dto.getServingPrice()));
        cateringItem.setModifiedAt(LocalDateTime.now());

        cateringItemRepository.save(cateringItem);

        log.info("UPDATED");

        return cateringItem;
    }

    public void delete(long id) {
        log.info("TRYING TO DELETE CATERING WITH ID " + id);
        cateringItemRepository.deleteById(id);
    }

}
