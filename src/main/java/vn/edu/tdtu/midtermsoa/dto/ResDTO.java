package vn.edu.tdtu.midtermsoa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ResDTO {
    private int code;
    private boolean status;
    private String message;
    private Object data;
}
