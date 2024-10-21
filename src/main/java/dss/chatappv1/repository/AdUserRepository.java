package dss.chatappv1.repository;

import dss.chatappv1.model.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdUserRepository extends JpaRepository<AdUser,Long> {
    Optional<AdUser> findByName(String name);

    Optional<AdUser> findByExtension(String destinationNumber);
}
