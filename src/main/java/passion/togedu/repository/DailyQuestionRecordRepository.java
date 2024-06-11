package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import passion.togedu.domain.DailyQuestionRecord;

@Repository
public interface DailyQuestionRecordRepository extends JpaRepository<DailyQuestionRecord, Integer> {

}