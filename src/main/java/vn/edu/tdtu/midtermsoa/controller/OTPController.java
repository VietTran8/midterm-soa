package vn.edu.tdtu.midtermsoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.dto.request.ValidateOTP;
import vn.edu.tdtu.midtermsoa.service.OTPService;

import java.security.Principal;

@RestController
@RequestMapping("api/otp")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;
    @PostMapping("/validate")
    public ResponseEntity<ResDTO> validate(@RequestBody ValidateOTP requestBody, Principal principal){
        ResDTO response = otpService.validateOTP(principal, requestBody);
        return response.isStatus()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }
}
