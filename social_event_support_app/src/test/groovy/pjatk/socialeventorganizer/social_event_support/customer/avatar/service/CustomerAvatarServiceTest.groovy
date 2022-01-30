package pjatk.socialeventorganizer.social_event_support.customer.avatar.service

import pjatk.socialeventorganizer.social_event_support.customer.avatar.model.CustomerAvatar
import pjatk.socialeventorganizer.social_event_support.customer.avatar.repository.CustomerAvatarRepository
import pjatk.socialeventorganizer.social_event_support.customer.repository.CustomerRepository
import pjatk.socialeventorganizer.social_event_support.trait.customer.CustomerTrait
import spock.lang.Specification
import spock.lang.Subject


class CustomerAvatarServiceTest extends Specification
        implements CustomerTrait {

    @Subject
    CustomerAvatarService customerAvatarService

    CustomerAvatarRepository customerAvatarRepository
    CustomerRepository customerRepository

    def setup() {
        customerAvatarRepository = Mock()
        customerRepository = Mock()

        customerAvatarService = new CustomerAvatarService(customerAvatarRepository, customerRepository)
    }

    def "DeleteById"() {
        given:
        def id = 1l
        def customer = fakeCustomer
        def avatarId = customer.getAvatar().getId();

        when:
        customerAvatarService.deleteById(id)

        then:
        1 * customerRepository.getByIdWithAvatar(id) >> Optional.of(customer)
        1 * customerRepository.save(customer)
        1 * customerAvatarRepository.deleteById(avatarId)

    }

    def "Save"() {
        given:
        def avatar = CustomerAvatar.builder()
                .image("image".getBytes())
                .fileName("name")
                .build()

        when:
        customerAvatarService.save(avatar)
        then:
        1 * customerAvatarRepository.save(avatar)

    }

    def "Delete"() {
        given:
        def avatar = CustomerAvatar.builder()
                .id(1L)
                .image("image".getBytes())
                .fileName("name")
                .build()

        when:
        customerAvatarService.delete(avatar)
        then:
        customerAvatarRepository.delete(avatar)
    }
}
