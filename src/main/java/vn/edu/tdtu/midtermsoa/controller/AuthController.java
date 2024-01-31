package vn.edu.tdtu.midtermsoa.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.dto.request.LoginDTO;
import vn.edu.tdtu.midtermsoa.service.AuthService;

import java.security.Principal;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<ResDTO> loginUser(@RequestBody LoginDTO loginDTO, HttpSession session){
        ResDTO response = authService.login(loginDTO, session);
        return response.isStatus()
                ? ResponseEntity.ok(response)
                : ResponseEntity.badRequest().body(response);
    }
}
