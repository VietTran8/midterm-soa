package vn.edu.tdtu.midtermsoa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.edu.tdtu.midtermsoa.model.Student;
import vn.edu.tdtu.midtermsoa.model.User;
import vn.edu.tdtu.midtermsoa.service.StudentService;
import vn.edu.tdtu.midtermsoa.service.UserService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserService userService;
    private final StudentService studentService;
    @Bean
    public CommandLineRunner runner(){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                List<User> users = userService.getAllUser();
                users.forEach(user -> {
                    user.setInTransaction(false);
                    userService.saveUser(user);
                });

                List<Student> students = studentService.getAllStudent();
                students.forEach(student -> {
                    student.setAccessQuantity(0);
                    studentService.saveStudent(student);
                });
            }
        };
    }
}
