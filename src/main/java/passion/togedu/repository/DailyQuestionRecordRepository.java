package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import passion.togedu.domain.DailyQuestionRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyQuestionRecordRepository extends JpaRepository<DailyQuestionRecord, Integer> {

    //userid로 날짜의 역순으로 조회
    @Query("SELECT dqr.date FROM DailyQuestionRecord dqr WHERE dqr.parent.id = :userId ORDER BY dqr.date DESC")
    List<LocalDateTime> findAllDatesByUserId(@Param("userId") Integer userId);

    //데이터 전체 개수 조회
    @Query("SELECT COUNT(dqr) FROM DailyQuestionRecord dqr WHERE dqr.parent.id = :userId")
    Integer countByUserId(@Param("userId") Integer userId);

    // 해당 날짜에 오늘의 질문 기록이 있는지 여부 확인
    @Query("SELECT COUNT(dqr) > 0 FROM DailyQuestionRecord dqr WHERE dqr.parent.id = :parentId AND DATE(dqr.date) = :date")
    boolean existsByParentIdAndDate(@Param("parentId") Integer parentId, @Param("date") LocalDate date);

    // 해당 번호의 질문 답변 기록이 있는지 여부 확인
    @Query("SELECT COUNT(dqr) > 0 FROM DailyQuestionRecord dqr WHERE dqr.parent.id = :parentId AND dqr.dailyQuestion.id = :questionId")
    boolean existsByParentIdAndDailyQuestion_Id(@Param("parentId") Integer parentId, @Param("questionId") Integer questionId);

    //userid로 날짜 내림차순으로 모든 데이터 찾기
    @Query("SELECT dqr FROM DailyQuestionRecord dqr WHERE dqr.parent.id = :userId ORDER BY dqr.date DESC")
    List<DailyQuestionRecord> findAllByUserId(@Param("userId") Integer userId);

    //userId와 QuestionId로 답변 데이터 찾기
    @Query("SELECT dqr FROM DailyQuestionRecord dqr WHERE dqr.parent.id = :userId AND dqr.dailyQuestion.id = :questionId")
    Optional<DailyQuestionRecord> findByUserIdAndQuestionId(@Param("userId") Integer userId, @Param("questionId") Integer questionId);

}