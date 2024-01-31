package vn.edu.tdtu.midtermsoa.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.midtermsoa.config.DateHandler;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.dto.request.CreatePaymentDTO;
import vn.edu.tdtu.midtermsoa.model.Student;
import vn.edu.tdtu.midtermsoa.model.Transaction;
import vn.edu.tdtu.midtermsoa.model.User;
import vn.edu.tdtu.midtermsoa.model.enums.ETransactionStatus;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final UserService userService;
    private final StudentService studentService;
    private final OTPService otpService;
    private final TransactionService transactionService;
    public ResDTO createPayment(Principal principal, CreatePaymentDTO dto){
        ResDTO response = ResDTO.builder()
                .code(HttpServletResponse.SC_UNAUTHORIZED)
                .message("You are not authenticated")
                .status(false)
                .data(null).build();

        if(principal != null){
            Transaction transaction = new Transaction();
            User foundUser = userService.getUserByUsername(principal.getName());
            Student foundStudent = studentService.getStudentByStudentNum(dto.getStudentNumber());
            if(foundUser != null){
                if(foundStudent.getAccessQuantity() == 1){
                    if(foundStudent.getTotalTuition() > 0)
                        if(otpService.sendOTP(foundUser)){
                            transaction.setCreatedAt(DateHandler.getCurrentDateTime());
                            transaction.setAmount(foundStudent.getTotalTuition());
                            transaction.setUser(foundUser);
                            transaction.setStatus(ETransactionStatus.PENDING);
                            transaction.setContent("Thanh toán học phí cho sinh viên: " + dto.getStudentNumber());

                            response.setData(transactionService.saveTransaction(transaction));
                            response.setStatus(true);
                            response.setMessage("Vui lòng nhập mã OTP gửi về email " + foundUser.getEmail() + " để tiếp tục thanh toán");
                            response.setCode(HttpServletResponse.SC_OK);
                        }else{
                            response.setData(null);
                            response.setStatus(false);
                            response.setMessage("Có lỗi xảy ra, vui lòng thử lại");
                            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                        }
                    else {
                        response.setData(null);
                        response.setStatus(false);
                        response.setMessage("Sinh viên này không còn nợ học phí!");
                        response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }else{
                    response.setData(null);
                    response.setStatus(false);
                    response.setMessage("Hiện đang có tài khoản khác đang thanh toán cho sinh viên này!");
                    response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }

        return response;
    }
}
