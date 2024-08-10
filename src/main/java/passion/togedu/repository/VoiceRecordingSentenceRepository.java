package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import passion.togedu.domain.VoiceRecordingSentence;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface VoiceRecordingSentenceRepository extends JpaRepository<VoiceRecordingSentence, Integer> {
    //userid와 상관없이 문장 데이터 개수 조회
    @Query("SELECT COUNT(vrs) FROM VoiceRecordingSentence vrs")
    Integer countTotalSentences();

    @Query("SELECT s FROM VoiceRecordingSentence s WHERE s.id NOT IN " +
            "(SELECT r.voiceRecordingSentence.id FROM VoiceRecordingRecord r WHERE r.parent.id = :parentId)")
    List<VoiceRecordingSentence> findUnrecordedSentencesByParentId(@Param("parentId") Integer parentId);
}
