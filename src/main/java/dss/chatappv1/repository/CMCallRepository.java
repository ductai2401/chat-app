package dss.chatappv1.repository;

import dss.chatappv1.model.CMCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CMCallRepository extends JpaRepository<CMCall, Long> {
    Optional<CMCall> findByCmCallId(BigDecimal cmCallId);
}
