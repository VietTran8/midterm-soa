package vn.edu.tdtu.midtermsoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.dto.request.CreatePaymentDTO;
import vn.edu.tdtu.midtermsoa.service.PaymentService;

import java.security.Principal;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(Principal principal, @RequestBody CreatePaymentDTO body){
        ResDTO response = paymentService.createPayment(principal, body);

        return response.isStatus()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }
}