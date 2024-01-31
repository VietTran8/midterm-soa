package vn.edu.tdtu.midtermsoa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.tdtu.midtermsoa.model.enums.ETransactionStatus;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String email;
    private String fullName;
    private String phone;
    private double balance;
    @JsonIgnore
    private boolean inTransaction;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OTP> otpList;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Transaction> transactions;

    public void clearOTPExpirationDate(){
        List<OTP> otps = this.getOtpList();
        otps.stream().filter(otp -> otp.getExpirationDate().getTime() > new Date().getTime())
                .forEach(otp -> otp.setExpirationDate(new Date()));
    }

    public List<Transaction> getUserTransactions(){
        return this.getTransactions().stream()
                .filter(t -> t.getStatus().equals(ETransactionStatus.COMPLETE))
                .sorted(new Comparator<Transaction>() {
                    @Override
                    public int compare(Transaction o1, Transaction o2) {
                        return Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime());
                    }
                })
                .toList();
    }
}
