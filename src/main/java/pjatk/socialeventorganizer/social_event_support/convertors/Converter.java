package pjatk.socialeventorganizer.social_event_support.convertors;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Slf4j
@UtilityClass
public class Converter {

    public BigDecimal convertPriceString(String inputPrice) {
        final String format = String.format("%.2f", Double.parseDouble(inputPrice));
        return new BigDecimal(format);
    }

    public BigInteger convertPhoneNumberString(String phoneNumber) {
        return new BigInteger(phoneNumber);
    }

    public String convertDescriptionsString(String description) {
        final Optional<String> optional = Optional.ofNullable(description)
                .filter(s -> !s.isEmpty());
        return optional.orElse(null);
    }
}
