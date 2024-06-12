package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import passion.togedu.domain.VoiceRecordingSentence;

public interface VoiceRecordingSentenceRepository extends JpaRepository<VoiceRecordingSentence, Integer> {
    //userid와 상관없이 문장 데이터 개수 조회
    @Query("SELECT COUNT(vrs) FROM VoiceRecordingSentence vrs")
    Integer countTotalSentences();
}
