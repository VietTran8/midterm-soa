package vn.edu.tdtu.midtermsoa.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.midtermsoa.config.DateHandler;
import vn.edu.tdtu.midtermsoa.dto.MailDetails;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.dto.request.ValidateOTP;
import vn.edu.tdtu.midtermsoa.model.OTP;
import vn.edu.tdtu.midtermsoa.model.Student;
import vn.edu.tdtu.midtermsoa.model.Transaction;
import vn.edu.tdtu.midtermsoa.model.User;
import vn.edu.tdtu.midtermsoa.model.enums.ETransactionStatus;
import vn.edu.tdtu.midtermsoa.repository.OTPRepository;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OTPService {
    private final OTPRepository otpRepository;
    private final MailService mailService;
    private final UserService userService;
    private final TransactionService transactionService;
    private final StudentService studentService;
    public boolean sendOTP(User user){
        OTP newOtp = OTP.create(user);

        boolean isSent = user.getEmail() != null && mailService.sendMail(
                MailDetails.builder()
                        .sendTo(user.getEmail())
                        .text("Mã OTP thanh toán học phí của bạn là: " + newOtp.getNumber())
                        .subject("Mã OTP")
                        .build()
        );

        if(isSent){
            user.clearOTPExpirationDate();
            newOtp.createExpirationDate();
            otpRepository.save(newOtp);
        }

        return isSent;
    }

    public ResDTO validateOTP(Principal principal, ValidateOTP requestBody){
        User foundUser = userService.getUserByUsername(principal.getName());
        List<OTP> otps = foundUser.getOtpList();

        List<OTP> matchedOTP = otps.stream().filter(item ->
                        item.getNumber().equals(requestBody.getOtp()) && item.getExpirationDate().getTime() >= new Date().getTime())
                .toList();

        if(matchedOTP.size() > 0){
            matchedOTP.forEach(item -> {
                item.setExpirationDate(new Date());
                otpRepository.save(item);
            });

            Transaction foundTransaction = transactionService.getTransactionById(requestBody.getTransactionId());
            if(foundTransaction != null){
                foundTransaction.setStatus(ETransactionStatus.COMPLETE);
                foundTransaction.setCreatedAt(DateHandler.getCurrentDateTime());
                foundUser.setBalance(foundUser.getBalance() - foundTransaction.getAmount());

                NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
                foundTransaction.setContent(foundTransaction.getContent() + "; Số dư: " + numberFormat.format(foundUser.getBalance()) + " VNĐ");
                transactionService.saveTransaction(foundTransaction);
            }

            Student foundStudent = studentService.getStudentByStudentNum(requestBody.getStudentNumber());
            if(foundStudent != null){
                foundStudent.completeTuition();
                studentService.saveStudent(foundStudent);
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedNumber = decimalFormat.format(foundTransaction.getAmount());

            mailService.sendMail(
                    MailDetails.builder()
                            .sendTo(foundUser.getEmail())
                            .text("Bạn đã thanh toán thành công học phí (" +
                                    formattedNumber + " VNĐ" +
                                    ") cho sinh viên: " + foundStudent.getStudentNumber() + " - " + foundStudent.getFullName())
                            .subject("Thanh toán thành công")
                            .build());

            return ResDTO.builder()
                    .code(HttpServletResponse.SC_OK)
                    .message("Mã OTP hợp lệ")
                    .status(true)
                    .data(null).build();
        }

        return ResDTO.builder()
                    .code(HttpServletResponse.SC_BAD_REQUEST)
                    .message("Mã OTP không hợp lệ")
                    .status(false)
                    .data(null).build();
    }
}
