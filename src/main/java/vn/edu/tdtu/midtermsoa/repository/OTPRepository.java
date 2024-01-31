package vn.edu.tdtu.midtermsoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.midtermsoa.model.OTP;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Integer> {
}
