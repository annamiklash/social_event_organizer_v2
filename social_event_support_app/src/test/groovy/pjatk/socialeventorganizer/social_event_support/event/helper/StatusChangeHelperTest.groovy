package pjatk.socialeventorganizer.social_event_support.event.helper

import pjatk.socialeventorganizer.social_event_support.trait.event.OrganizedEventTrait
import spock.lang.Specification
import spock.lang.Subject

class StatusChangeHelperTest extends Specification implements OrganizedEventTrait {

    @Subject
    StatusChangeHelper statusChangeHelper

    def setup() {
        statusChangeHelper = new StatusChangeHelper()
    }

    def "PossibleToChangeStatusFromInProgressToConfirmed"() {
        when:
        def result = statusChangeHelper.possibleToChangeStatusFromInProgressToConfirmed(organizedEvent)

        then:
        result == target

        where:
        organizedEvent                                                                                                                                                     | target
        fakeOrganizedEvent.withEventStatus("CONFIRMED")                                                                                                                    | false
        fakeOrganizedEvent.withLocationForEvent(null)                                                                                                                      | false
        fakeOrganizedEvent.withLocationForEvent(Set.of(fakeOrganizedEvent.getLocationForEvent().iterator().next().withConfirmationStatus('NOT_CONFIRMED')))                | false
        fakeOrganizedEvent.withLocationForEvent(Set.of(fakeOrganizedEvent.getLocationForEvent().iterator().next().withCateringsForEventLocation(null).withServices(null))) | true
        fakeOrganizedEvent                                                                                                                                                 | true
    }
}
