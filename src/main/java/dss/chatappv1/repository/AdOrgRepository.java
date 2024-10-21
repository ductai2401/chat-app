package dss.chatappv1.repository;

import dss.chatappv1.model.AdOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AdOrgRepository extends JpaRepository<AdOrg,Long> {

    Optional<AdOrg> findByValue(String value);
}
