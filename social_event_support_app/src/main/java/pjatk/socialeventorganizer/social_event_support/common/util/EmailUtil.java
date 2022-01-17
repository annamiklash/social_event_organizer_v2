package pjatk.socialeventorganizer.social_event_support.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.mail.SimpleMailMessage;

import static pjatk.socialeventorganizer.social_event_support.common.constants.Const.APP_EMAIL;

@UtilityClass
public class EmailUtil {


    public SimpleMailMessage buildEmail(String content, String sendTo, String subject, String replyToEmail) {

        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(APP_EMAIL);
        simpleMailMessage.setTo(sendTo);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        if (replyToEmail != null) {
            simpleMailMessage.setReplyTo(replyToEmail);
        }

        return simpleMailMessage;
    }
}
