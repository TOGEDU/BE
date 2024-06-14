package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.ParentChild;

import java.util.Optional;

public interface ParentChildRepository extends JpaRepository<ParentChild, Integer> {
    boolean existsByUniqueCode(String uniqueCode);
    Optional<ParentChild> findByUniqueCode(String uniqueCode);}
