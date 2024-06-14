package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.Parent;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Integer> {
    Optional<Parent> findByParentCode(String parentCode);
    Optional<Parent> findById(Integer id);
    boolean existsByEmail(String email);
    Optional<Parent> findByEmail(String email);
}
