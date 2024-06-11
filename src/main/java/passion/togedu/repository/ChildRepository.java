package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.Child;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Integer> {
    Optional<Child> findById(Integer id);
    boolean existsByEmail(String email);
    Optional<Child> findByEmail(String email);
}
