package vn.edu.tdtu.midtermsoa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String studentNumber;
    private String fullName;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tuition> tuitionList;
    private int accessQuantity;
    @Transient
    private double totalTuition;

    public void completeTuition(){
        this.getTuitionList()
                .stream().filter(tuition -> !tuition.isStatus())
                .forEach(tuition -> tuition.setStatus(true));
    }

    public double getTotalTuition(){
        return this.getTuitionList().stream()
                .filter(t -> !t.isStatus())
                .map(Tuition::getAmount)
                .reduce(Double::sum)
                .orElse(0.0);
    }
}
