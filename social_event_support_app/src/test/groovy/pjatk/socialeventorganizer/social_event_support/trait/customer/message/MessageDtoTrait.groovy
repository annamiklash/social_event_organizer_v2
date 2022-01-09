package pjatk.socialeventorganizer.social_event_support.trait.customer.message

import pjatk.socialeventorganizer.social_event_support.customer.message.dto.MessageDto

trait MessageDtoTrait {

    MessageDto fakeMessageDto = MessageDto.builder()
            .subject("SAMPLE SUBJECGT")
            .content("SAMPLE CONTENT")
            .receiverEmail("receiver@email.com")
            .build()

}