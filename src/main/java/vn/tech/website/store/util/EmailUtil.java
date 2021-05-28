
package vn.tech.website.store.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.tech.website.store.dto.common.MailDto;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

@Getter
@Setter
public class EmailUtil implements Runnable {
    private static final String UTF8_CHARACTER = "text/html; charset=UTF-8";

    private static EmailUtil INSTANCE = null;


    private SmtpAuthenticator smtpAuthenticator;
    private Queue<MailDto> mailDtoQueue;

    private static Logger log = LoggerFactory.getLogger(EmailUtil.class);

    public static EmailUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EmailUtil();
            new Thread(INSTANCE).start();
        }
        return INSTANCE;
    }

    public EmailUtil() {
        String email = PropertiesUtil.getEmailProperty("mail.user");
        String password = PropertiesUtil.getEmailProperty("mail.password");
        smtpAuthenticator = new SmtpAuthenticator(email, password);
        mailDtoQueue = new LinkedList<>();
    }

    public boolean sendPasswordUserEmail(String loaiTaiKhoan, String emailTo, String password, String userName, String fullName) {
        String subject = PropertiesUtil.getProperty("email.createUser.subject");
        String content = PropertiesUtil.getProperty("email.createUser.content")
                .replace("{LOAI_TAI_KHOAN}", loaiTaiKhoan)
                .replace("{TEN_TAI_KHOAN}", emailTo)
                .replace("{FULL_NAME}", fullName)
                .replace("{USER_NAME}", userName)
                .replace("{MAT_KHAU}", password);
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    public boolean sendCreateUserEmail(String emailTo, String link, String fullName) {
        String subject = PropertiesUtil.getProperty("email.createNewUser.subject");
        String content = PropertiesUtil.getProperty("email.createNewUser.content")
                .replace("{TEN_TAI_KHOAN}", fullName)
                .replace("{LINK}", link);
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    public boolean sendLostPasswordEmail(String emailTo, boolean check, String shopName, String username, String verifyCode, String adminName, String email, String newPass, String urlImage) {
        String subject;
        String content;
        if (check) {
            subject = PropertiesUtil.getProperty("email.lostPasswordGetCode.subject")
                    .replace("{SHOP_NAME}", shopName.toUpperCase())
                    .replace("{USER_NAME}", username);
            content = PropertiesUtil.getProperty("email.lostPasswordGetCode.content")
                    .replace("{USER_NAME}", username.toUpperCase())
                    .replace("{VERIFY_CODE}", verifyCode)
                    .replace("{ADMIN}", adminName)
                    .replace("{SHOP_NAME}", shopName)
                    .replace("{URL_IMAGE}", urlImage);
        } else {
            subject = PropertiesUtil.getProperty("email.lostPasswordUpdate.subject")
                    .replace("{SHOP_NAME}", shopName.toUpperCase());
            content = PropertiesUtil.getProperty("email.lostPasswordUpdate.content")
                    .replace("{USER_NAME}", username.toUpperCase())
                    .replace("{CUSTOMER_EMAIL}", email)
                    .replace("{NEW_PASS}", newPass)
                    .replace("{ADMIN}", adminName)
                    .replace("{SHOP_NAME}", shopName)
                    .replace("{URL_IMAGE}", urlImage);
        }
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    public boolean sendConfirmChangeEmail(String emailTo,String userName,String code,String shopName, String admin, String urlImage) {
        String subject = PropertiesUtil.getProperty("email.changeEmail.subject")
                .replace("{SHOP_NAME}", shopName.toUpperCase());
        String content = PropertiesUtil.getProperty("email.changeEmail.content")
                .replace("{USER_NAME}", userName)
                .replace("{EMAIL}",emailTo)
                .replace("{CODE}", code)
                .replace("{SHOP_NAME}",shopName.toUpperCase())
                .replace("{ADMIN}",admin)
                .replace("{URL_IMAGE}", urlImage);
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    public boolean sendVerifyCodeToRegister(String emailTo,String userName,String code, String shopName, String admin, String urlImage) {
        String subject = PropertiesUtil.getProperty("email.register.subject")
                .replace("{SHOP_NAME}", shopName.toUpperCase());
        String content = PropertiesUtil.getProperty("email.register.content")
                .replace("{USER_NAME}", userName)
                .replace("{EMAIL}",emailTo)
                .replace("{CODE}", code)
                .replace("{SHOP_NAME}",shopName.toUpperCase())
                .replace("{ADMIN}",admin)
                .replace("{URL_IMAGE}", urlImage);
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    public boolean sendNotificationApprovedOrder(String emailTo,String userName,String orderCode,Date orderTime,String shopName, String admin, String urlImage) {
        String subject = PropertiesUtil.getProperty("email.approvedOrder.subject")
                .replace("{SHOP_NAME}",shopName.toUpperCase());
        String content = PropertiesUtil.getProperty("email.approvedOrder.content")
                .replace("{USER_NAME}", userName)
                .replace("{ORDER_CODE}", orderCode)
                .replace("{ORDER_TIME}", Objects.requireNonNull(DateUtil.formatToPattern(orderTime, DateUtil.DATE_FORMAT)))
                .replace("{SHOP_NAME}",shopName.toUpperCase())
                .replace("{ADMIN}",admin)
                .replace("{URL_IMAGE}", urlImage);
        return mailDtoQueue.add(new MailDto(emailTo, subject, content));
    }

    private boolean send(MailDto mailDto) {
        try {
            Properties emailProps = new Properties();
            emailProps.load(PropertiesUtil.class.getResourceAsStream("/email.properties"));
            // Get the default Session object.
            Session session = Session.getDefaultInstance(emailProps, smtpAuthenticator);
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(PropertiesUtil.getEmailProperty("mail.user")));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailDto.getEmailTo()));
            // Set Subject: header field
            message.setSubject(mailDto.getSubject(), "UTF-8");
            // Send the actual HTML message, as big as you like
            message.setHeader("Content-Type", UTF8_CHARACTER);

            if (StringUtils.isNotBlank(mailDto.getFileName())) {
                // Create a multipar message
                Multipart multipart = new MimeMultipart();

                // Create the message part
                BodyPart bodyPart = new MimeBodyPart();
                bodyPart.setContent(mailDto.getContent(), UTF8_CHARACTER);
                multipart.addBodyPart(bodyPart);

                // Create attached file
                bodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(mailDto.getFilePath());
                bodyPart.setDataHandler(new DataHandler(source));
                bodyPart.setFileName(mailDto.getFileName());
                multipart.addBodyPart(bodyPart);

                message.setContent(multipart);
            } else {
                message.setContent(mailDto.getContent(), UTF8_CHARACTER);
            }
            // Send message
            Transport.send(message);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                MailDto mailDto = mailDtoQueue.poll();
                if (mailDto != null) {
                    String rs = send(mailDto) ? "success" : "fail";
                    log.info("Send mail is " + rs + " (" + mailDto + ")");
                }
            } catch (Exception e) {
                log.error("Lỗi", e);
            }
        }
    }

//    public static void main(String[] args) {
//      EmailUtil.getInstance().sendConfirmChangeEmail("o2xallblue@gmail.com","niet","123456");
//    }
}
