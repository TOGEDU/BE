package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.DailyQuestion;
import java.util.Optional;


public interface DailyQuestionRepository extends JpaRepository<DailyQuestion, Integer> {
    Optional<DailyQuestion> findById(Integer id);
}
