package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import passion.togedu.domain.Child;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Integer> {
    Optional<Child> findById(Integer id);
    boolean existsByEmail(String email);
    Optional<Child> findByEmail(String email);
    @Query("SELECT u FROM Child u WHERE FUNCTION('TIME_FORMAT', u.pushNotificationTime, '%H:%i') = :currentTime AND u.pushStatus = true AND u.fcmToken IS NOT NULL")
    List<Child> findChildrenWithCurrentPushTimeAndStatus(@Param("currentTime") String currentTime);

}
