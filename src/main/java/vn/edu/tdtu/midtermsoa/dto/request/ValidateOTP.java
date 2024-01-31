package vn.edu.tdtu.midtermsoa.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOTP {
    private String otp;
    private long transactionId;
    private String studentNumber;
}
