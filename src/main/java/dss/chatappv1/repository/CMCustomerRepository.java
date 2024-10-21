package dss.chatappv1.repository;

import dss.chatappv1.model.CMCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CMCustomerRepository extends JpaRepository<CMCustomer, Long> {
    Optional<CMCustomer> findByPhone(String phone);
}
