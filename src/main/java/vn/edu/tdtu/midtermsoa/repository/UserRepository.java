package vn.edu.tdtu.midtermsoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.tdtu.midtermsoa.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
