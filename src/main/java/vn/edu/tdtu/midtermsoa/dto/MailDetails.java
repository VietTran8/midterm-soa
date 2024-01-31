package vn.edu.tdtu.midtermsoa.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailDetails {
    private String subject;
    private String text;
    private String sendTo;
}
