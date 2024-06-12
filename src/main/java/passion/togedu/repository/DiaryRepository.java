package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import passion.togedu.domain.Diary;

import java.time.LocalDate;


@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {

    // parentChild의 parent를 기준으로 작성된 일기의 날짜 개수를 조회
    @Query("SELECT COUNT(DATE(d.date)) FROM Diary d WHERE d.parentChild.parent.id = :userId")
    int countAllDatesByUserId(Integer userId);

    //데이터 전체 개수 조회
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.parentChild.parent.id = :userId")
    Integer countByUserId(@Param("userId") Integer userId);

    //해당 날짜에 일기 기록이 있는지 여부 확인
    @Query("SELECT COUNT(d) > 0 FROM Diary d WHERE d.parentChild.parent.id = :parentId AND DATE(d.date) = :date")
    boolean existsByParentIdAndDate(@Param("parentId") Integer parentId, @Param("date") LocalDate date);
}
