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
        organizedEvent                                                                                                                           | target
        fakeOrganizedEvent.withEventStatus("CONFIRMED")                                                                                          | false
        fakeOrganizedEvent.withLocationForEvent(null)                                                                                            | false
        fakeOrganizedEvent.withLocationForEvent(fakeOrganizedEvent.getLocationForEvent().withConfirmationStatus('NOT_CONFIRMED'))                | false
        fakeOrganizedEvent.withLocationForEvent(fakeOrganizedEvent.getLocationForEvent().withCateringsForEventLocation(null).withServices(null)) | true
        fakeOrganizedEvent                                                                                                                       | true
    }
}
