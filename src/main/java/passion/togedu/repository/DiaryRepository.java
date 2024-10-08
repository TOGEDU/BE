package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import passion.togedu.domain.Diary;
import passion.togedu.domain.ParentChild;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {

    //해당 날짜에 일기 기록이 있는지 여부 확인
    @Query("SELECT COUNT(d) > 0 FROM Diary d WHERE d.parentChild.parent.id = :parentId AND DATE(d.date) = :date")
    boolean existsByParentIdAndDate(@Param("parentId") Integer parentId, @Param("date") LocalDate date);

    List<Diary> findByParentChild(ParentChild parentChild);

    // 특정 ParentChildId들과 날짜 범위 내에 있는 일기를 모두 조회하는 메서드
    @Query("SELECT d FROM Diary d WHERE d.parentChild.id IN :parentChildIdList AND d.date BETWEEN :startDate AND :endDate")
    List<Diary> findDiariesByParentChildIdsAndDateBetween(List<Integer> parentChildIdList, LocalDate startDate, LocalDate endDate);

    // 특정 ParentChildId들과 날짜에 해당하는 일기를 조회하는 메서드
    @Query("SELECT d FROM Diary d WHERE d.parentChild.id IN :parentChildIdList AND d.date = :date")
    List<Diary> findDiariesByParentChildIdsAndDate(@Param("parentChildIdList") List<Integer> parentChildIdList, @Param("date") LocalDate date);

    @Query("SELECT d.parentChild.id FROM Diary d WHERE d.parentChild.id IN :parentChildIdList AND d.date = :date")
    List<Integer> findParentChildIdsWithDiaryOnDate(@Param("parentChildIdList") List<Integer> parentChildIdList, @Param("date") LocalDate date);

    // 특정 ParentChild ID와 날짜로 이미 작성된 일기가 있는지 확인하는 메서드
    Optional<Diary> findByParentChildIdAndDate(Integer parentChildId, LocalDate date);
}
