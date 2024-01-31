package vn.edu.tdtu.midtermsoa.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.tdtu.midtermsoa.service.StudentService;

import java.util.logging.Logger;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
    private final StudentService studentService;
    @GetMapping
    public String home(HttpSession session){
        studentService.clearStudentSession(session);
        String currentUser = (String) session.getAttribute("currentUser");
        Logger.getLogger("UserSession - [Home]").info(currentUser);

        return (currentUser != null && !currentUser.isEmpty()) ? "home" : "redirect:/login";
    }
}
