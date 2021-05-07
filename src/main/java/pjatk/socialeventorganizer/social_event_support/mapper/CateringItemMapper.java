package pjatk.socialeventorganizer.social_event_support.mapper;

import org.springframework.stereotype.Component;
import pjatk.socialeventorganizer.social_event_support.convertors.Converter;
import pjatk.socialeventorganizer.social_event_support.model.dto.CateringItem;
import pjatk.socialeventorganizer.social_event_support.model.request.CateringItemRequest;
import pjatk.socialeventorganizer.social_event_support.model.response.CateringItemResponse;


@Component
public class CateringItemMapper {

    public CateringItem mapToDTO(CateringItemRequest request) {
        return CateringItem.builder()
                .name(request.getName())
                .description(Converter.convertDescriptionsString(request.getDescription()))
                .servingPrice(Converter.convertPriceString(request.getServingPrice()))
                .isVegan(request.isVegan())
                .isVegetarian(request.isVegetarian())
                .isGlutenFree(request.isGlutenFree())
                .itemType(request.getType())
                .cateringId(request.getCateringId())
                .build();
    }

    public CateringItemResponse mapToResponse(CateringItem cateringItem) {
        return CateringItemResponse.builder()
                .id(cateringItem.getId())
                .name(cateringItem.getName())
                .price(cateringItem.getServingPrice())
                .type(cateringItem.getItemType())
                .build();

    }

    public CateringItem mapToDTO(CateringItemRequest request, Long cateringItemId) {
        return CateringItem.builder()
                .id(cateringItemId)
                .name(request.getName())
                .description(Converter.convertDescriptionsString(request.getDescription()))
                .servingPrice(Converter.convertPriceString(request.getServingPrice()))
                .isVegan(request.isVegan())
                .isVegetarian(request.isVegetarian())
                .isGlutenFree(request.isGlutenFree())
                .itemType(request.getType())
                .cateringId(request.getCateringId())
                .build();

    }
}
