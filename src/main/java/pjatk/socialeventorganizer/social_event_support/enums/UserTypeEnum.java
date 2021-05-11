package pjatk.socialeventorganizer.social_event_support.enums;

import lombok.Getter;

@Getter
public enum UserTypeEnum {

    CUSTOMER('C'),
    BUSINESS('B'),
    ADMIN('A');

    private final Character value;

    private UserTypeEnum(Character value) {
        this.value = value;
    }
}
