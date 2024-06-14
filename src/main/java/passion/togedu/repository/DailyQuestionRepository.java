package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import passion.togedu.domain.Child;
import passion.togedu.domain.DailyQuestion;

import java.util.Optional;


public interface DailyQuestionRepository extends JpaRepository<DailyQuestion, Integer> {
    Optional<DailyQuestion> findById(Integer id);
}
