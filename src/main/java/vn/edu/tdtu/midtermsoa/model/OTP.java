package vn.edu.tdtu.midtermsoa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(unique = true)
    private String number;
    private Date expirationDate;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId")
    private User user;

    public static OTP create(User user){
        OTP otp = new OTP();
        otp.setUser(user);

        SecureRandom random = new SecureRandom();
        int otpInt = random.nextInt(999999 - 100000 + 1) + 100000;

        otp.setNumber(String.valueOf(otpInt));

        return otp;
    }

    public void createExpirationDate(){
        Date currentDate = new Date();

        int expirationPeriodInMinutes = 3;

        Instant currentInstant = currentDate.toInstant();
        Instant expirationInstant = currentInstant.plusSeconds(expirationPeriodInMinutes * 60);

        this.setExpirationDate(Date.from(expirationInstant));
    }
}
