package vn.edu.tdtu.midtermsoa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.midtermsoa.model.Tuition;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentRespDTO {
    private long id;
    private String studentNumber;
    private String fullName;
    private List<Tuition> tuitionList;
    private int accessQuantity;
    private double totalTuition;
}
