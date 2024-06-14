package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.Diary;
import passion.togedu.domain.ParentChild;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findByDate(LocalDate date);
    List<Diary> findByParentChildAndDate(ParentChild parentChild, LocalDate date);
}
