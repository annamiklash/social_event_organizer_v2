package pjatk.socialeventorganizer.social_event_support.test_helper

import com.fasterxml.jackson.databind.ObjectMapper

class TestSerializer {

    def static serialize(Object objectToSerialize) {
        return new ObjectMapper().writeValueAsString(objectToSerialize)
    }
}
