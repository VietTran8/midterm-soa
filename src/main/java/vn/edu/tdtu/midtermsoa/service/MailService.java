package vn.edu.tdtu.midtermsoa.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.midtermsoa.dto.MailDetails;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    public boolean sendMail(MailDetails details){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(sender, "iBanking");
            helper.setTo(details.getSendTo());
            helper.setText(details.getText());
            helper.setSubject(details.getSubject());

            mailSender.send(message);
            return true;
        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
