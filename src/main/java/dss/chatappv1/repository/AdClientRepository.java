package dss.chatappv1.repository;

import dss.chatappv1.model.AdClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdClientRepository extends JpaRepository<AdClient, Long> {
    Optional<AdClient> findByValue(String value);
}
