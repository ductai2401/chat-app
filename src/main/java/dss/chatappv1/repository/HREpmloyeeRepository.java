package dss.chatappv1.repository;

import dss.chatappv1.model.HREmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HREpmloyeeRepository extends JpaRepository<HREmployee, Long> {
    Optional<HREmployee> findByExtension(String extension);
}
