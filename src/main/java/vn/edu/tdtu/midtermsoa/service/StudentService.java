package vn.edu.tdtu.midtermsoa.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.midtermsoa.dto.ResDTO;
import vn.edu.tdtu.midtermsoa.model.Student;
import vn.edu.tdtu.midtermsoa.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public Student getStudentByStudentNum(String stuNum){
        return studentRepository.findByStudentNumber(stuNum).orElse(null);
    }

    public List<Student> getAllStudent(){
        return studentRepository.findAll();
    }
    public Student saveStudent(Student student){
        return studentRepository.save(student);
    }
    public ResDTO getStudentRespByStudentNumber(String studentNumber, HttpSession session){
        Optional<Student> student = studentRepository.findByStudentNumber(studentNumber);
        if(student.isPresent()){
            Student studentIns = student.get();

            updateCurrentStudent(studentIns, session);

            return ResDTO.builder()
                    .code(HttpServletResponse.SC_OK)
                    .status(true)
                    .data(studentIns)
                    .message("Student info fetched successfully!")
                    .build();
        }

        clearStudentSession(session);

        return ResDTO.builder()
                .code(HttpServletResponse.SC_BAD_REQUEST)
                .message("Student not found!")
                .data(null)
                .status(false)
                .build();
    }

    private void updateCurrentStudent(Student newStudent, HttpSession session){
        String currentStudentNum = (String) session.getAttribute("currentStudent");
        Logger.getLogger("StudentSession - [Update]").info(currentStudentNum);

        boolean needToUpdate = false;

        if(currentStudentNum == null){
            needToUpdate = true;
        }
        else if(!currentStudentNum.equals(newStudent.getStudentNumber())){
            reduceStudentAccess(currentStudentNum);
            needToUpdate = true;
        }

        if(needToUpdate){
            session.setAttribute("currentStudent", newStudent.getStudentNumber());
            newStudent.setAccessQuantity(newStudent.getAccessQuantity() + 1);
            studentRepository.save(newStudent);
        }
    }

    public void clearStudentSession(HttpSession session){
        String currentStudentNum = (String) session.getAttribute("currentStudent");
        Logger.getLogger("StudentSession - [Clear]").info(currentStudentNum);

        if(currentStudentNum != null){
            if(reduceStudentAccess(currentStudentNum))
                session.setAttribute("currentStudent", null);
        }
    }

    public boolean reduceStudentAccess(String studentNumber){
        AtomicBoolean result = new AtomicBoolean(false);
        if(studentNumber != null) {
            studentRepository.findByStudentNumber(studentNumber).ifPresent(std -> {
                if (std.getAccessQuantity() > 0) {
                    std.setAccessQuantity(std.getAccessQuantity() - 1);
                    studentRepository.save(std);
                }
            });
            result.set(true);
        }
        return result.get();
    }
}
