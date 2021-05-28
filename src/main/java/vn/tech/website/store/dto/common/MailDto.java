package vn.tech.website.store.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MailDto {
    private String emailTo;
    private String subject;
    private String content;
    private String fileName;
    private String filePath;
//    private boolean status;
    public MailDto() {
    }

    public MailDto(String emailTo, String subject, String content) {
        this.emailTo = emailTo;
        this.subject = subject;
        this.content = content;
    }

    @Override
    public String toString() {
        return "To: " + emailTo + ", Subject: " + subject + ", Content: " + content;
    }
}
