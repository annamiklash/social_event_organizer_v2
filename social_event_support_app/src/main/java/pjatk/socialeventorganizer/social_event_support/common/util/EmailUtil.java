package pjatk.socialeventorganizer.social_event_support.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.mail.SimpleMailMessage;

@UtilityClass
public class EmailUtil {

    private final static String fromEmail = "testsocialeventorg@gmail.com";
//TODO: extract methods here

    public SimpleMailMessage emailBuilder(String content, String sendTo, String subject) {

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        return simpleMailMessage;

    }
}
