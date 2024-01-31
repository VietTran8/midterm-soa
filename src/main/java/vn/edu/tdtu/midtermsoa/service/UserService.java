package vn.edu.tdtu.midtermsoa.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.model.User;
import vn.edu.tdtu.midtermsoa.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public ResDTO getUserInfo(Principal principal){
        ResDTO response = ResDTO.builder()
                .code(HttpServletResponse.SC_UNAUTHORIZED)
                .message("You are not authenticated")
                .data(null)
                .status(false)
                .build();

        if(principal != null){
            String username = principal.getName();
            User foundUser = userRepository.findByUsername(username).orElse(null);

            if(foundUser != null){
                response.setStatus(true);
                response.setCode(HttpServletResponse.SC_OK);
                response.setMessage("User fetched successfully");
                response.setData(foundUser);
            }
        }

        return response;
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }
}
