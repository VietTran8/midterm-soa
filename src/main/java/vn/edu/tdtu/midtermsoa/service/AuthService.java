package vn.edu.tdtu.midtermsoa.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.dto.request.LoginDTO;
import vn.edu.tdtu.midtermsoa.model.User;
import vn.edu.tdtu.midtermsoa.repository.UserRepository;
import vn.edu.tdtu.midtermsoa.security.CustomUserDetail;

import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final StudentService studentService;
    private final DaoAuthenticationProvider authenticationProvider;
    private final TransactionService transactionService;

    public ResDTO login(LoginDTO request, HttpSession session) {
        ResDTO response = new ResDTO();
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        if (authentication.isAuthenticated()) {
            User authenticatedUser = ((CustomUserDetail) authentication.getPrincipal()).getUser();
            HashMap<String, Object> data = new HashMap<>();

            String token = Base64
                    .getEncoder()
                    .encodeToString(
                            (request.getUsername() + ":" + request.getPassword()).getBytes()
                    );

            data.put("user", authenticatedUser);
            data.put("token", token);
            data.put("tokenType", "Basic");

            response.setStatus(true);
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(data);
            response.setMessage("Logged in successfully");

            Optional<User> foundUser = userRepository.findByUsername(authenticatedUser.getUsername());
            if (foundUser.isPresent()) {
                User user = foundUser.get();

                if (!transactionService.beginTransaction(user.getUsername())) {
                    response.setStatus(false);
                    response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                    response.setData(null);
                    response.setMessage("Người dùng này đang trong một phiên giao dịch!");
                } else {
                    session.setAttribute("currentUser", user.getUsername());
                }
            }
        }
        return response;
    }
}
