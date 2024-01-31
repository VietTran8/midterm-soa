package vn.edu.tdtu.midtermsoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.tdtu.midtermsoa.model.Tuition;

@Repository
public interface TuitionRepository extends JpaRepository<Tuition, Long> {
}
