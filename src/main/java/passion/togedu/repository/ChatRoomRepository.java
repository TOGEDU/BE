package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.ChatRoom;
import passion.togedu.domain.DailyQuestion;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findById(Integer id);
    Optional<ChatRoom> findByIdAndChild_Id(Integer id, Integer childId);

    List<ChatRoom> findByChildId(Integer childId);

}