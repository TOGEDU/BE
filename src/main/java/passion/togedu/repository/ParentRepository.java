package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passion.togedu.domain.Parent;

import java.util.List;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Integer> {
    Optional<Parent> findByParentCode(String parentCode);
    Optional<Parent> findById(Integer id);
    boolean existsByEmail(String email);
    Optional<Parent> findByEmail(String email);

    @Query("SELECT u FROM Parent u WHERE FUNCTION('TIME_FORMAT', u.pushNotificationTime, '%H:%i') = :currentTime AND u.pushStatus = true AND u.fcmToken IS NOT NULL")
    List<Parent> findParentsWithCurrentPushTimeAndStatus(@Param("currentTime") String currentTime);

}
