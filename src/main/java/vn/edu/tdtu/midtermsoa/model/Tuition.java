package vn.edu.tdtu.midtermsoa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tuition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String content;
    private double amount;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "studentId")
    @JsonIgnore
    private Student student;
}
