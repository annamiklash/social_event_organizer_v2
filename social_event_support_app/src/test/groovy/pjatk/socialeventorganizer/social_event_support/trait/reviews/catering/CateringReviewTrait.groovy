package pjatk.socialeventorganizer.social_event_support.trait.reviews.catering

import com.google.common.collect.ImmutableSet
import pjatk.socialeventorganizer.social_event_support.address.model.Address
import pjatk.socialeventorganizer.social_event_support.catering.model.Catering
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine
import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar
import pjatk.socialeventorganizer.social_event_support.customer.model.Customer
import pjatk.socialeventorganizer.social_event_support.reviews.catering.model.CateringReview

import java.time.LocalDate
import java.time.LocalDateTime

trait CateringReviewTrait {

    CateringReview fakeCateringReview = CateringReview.builder()
            .id(1L)
            .title("SAMPLE TITLE")
            .comment("SAMPLE COMMENT")
            .createdAt(LocalDateTime.parse('2007-12-03T10:15:30'))
            .starRating(5)
            .customer(Customer.builder()
                    .id(1L)
                    .firstName('Geralt')
                    .lastName('Rivijski')
                    .birthdate(LocalDate.parse('2007-12-03'))
                    .phoneNumber(new BigInteger("123123123"))
                    .email('email@email.com')
                    .guests(new HashSet<>())
                    .events(new HashSet<>())
                    .appProblems(new HashSet<>())
                    .avatar(CustomerAvatar.builder().id(1L).build())
                    .build())
            .catering(Catering.builder()
                    .id(1)
                    .name('Name')
                    .email('catering@email.com')
                    .phoneNumber(new BigInteger('123456789'))
                    .description('description')
                    .cateringAddress(Address.builder()
                            .id(1)
                            .country('Poland')
                            .city('Warsaw')
                            .streetName('Piękna')
                            .streetNumber(1)
                            .zipCode('01-157')
                            .build())
                    .cuisines(ImmutableSet.of(
                            Cuisine.builder()
                                    .id(1)
                                    .name('Greek')
                                    .build()
                    ))
                    .build())
            .build()
}
