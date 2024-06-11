package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.Child;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Integer> {
    Optional<Child> findById(Integer id);
    boolean existsByEmail(String email);
    List<Child> findByEmail(String email);
}
