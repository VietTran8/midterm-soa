package vn.edu.tdtu.midtermsoa.config;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.tdtu.midtermsoa.service.StudentService;
import vn.edu.tdtu.midtermsoa.service.TransactionService;

import java.util.logging.Logger;

@WebListener
@Component
@RequiredArgsConstructor
public class MySessionListener implements HttpSessionListener {
    private final StudentService studentService;
    private final TransactionService transactionService;
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        String currentStudentNum = (String) session.getAttribute("currentStudent");
        String currentUsername = (String) session.getAttribute("currentUser");
        Logger.getLogger("StudentSession - [Listener]").info(currentStudentNum);
        Logger.getLogger("UserSession - [Listener]").info(currentUsername);

        if(currentStudentNum != null){
            studentService.reduceStudentAccess(currentStudentNum);
        }
        if(currentUsername != null){
            transactionService.closeTransaction(currentUsername);
        }
        HttpSessionListener.super.sessionDestroyed(se);
    }
}
