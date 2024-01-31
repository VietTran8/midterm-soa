package vn.edu.tdtu.midtermsoa.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String loginPage(HttpSession session){
        return session.getAttribute("currentUser") == null ? "login-page" : "redirect:/home";
    }
}
