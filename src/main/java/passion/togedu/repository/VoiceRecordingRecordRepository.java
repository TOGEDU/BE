package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import passion.togedu.domain.VoiceRecordingRecord;

import java.time.LocalDate;
@Repository
public interface VoiceRecordingRecordRepository extends JpaRepository<VoiceRecordingRecord, Integer> {
    //userid에 따라 녹음 기록 데이터 개수 조회
    @Query("SELECT COUNT(vrr) FROM VoiceRecordingRecord vrr WHERE vrr.parent.id = :userId")
    Integer countByUserId(@Param("userId") Integer userId);

    //해당 날짜에 음성기록이 있는지 여부 확인
    @Query("SELECT COUNT(vrr) > 0 FROM VoiceRecordingRecord vrr WHERE vrr.parent.id = :parentId AND DATE(vrr.date) = :date")
    boolean existsByParentIdAndDate(@Param("parentId") Integer parentId, @Param("date") LocalDate date);

}
