package pjatk.socialeventorganizer.social_event_support.reviews.catering_review.service;


import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.common.paginator.CustomPage;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.model.CateringReview;
import pjatk.socialeventorganizer.social_event_support.reviews.catering_review.repository.CateringReviewRepository;

import java.util.stream.Collectors;
@Service
@AllArgsConstructor
@Slf4j
public class CateringReviewService {

    private final CateringReviewRepository cateringReviewRepository;

    public void save(CateringReview cateringReview) {
        cateringReviewRepository.save(cateringReview);
    }


    public ImmutableList<CateringReview> listByCatering(CustomPage customPagination, Long keyword) {
        //keyword = Strings.isNullOrEmpty(keyword) ? "" : keyword.toLowerCase();

        final Pageable paging = PageRequest.of(customPagination.getFirstResult(), customPagination.getMaxResult(), Sort.by(customPagination.getSort()).descending());
        final Page<CateringReview> page = cateringReviewRepository.findCateringReviewByCateringId(paging, keyword);

        return ImmutableList.copyOf(page.get().collect(Collectors.toList()));
    }

}
