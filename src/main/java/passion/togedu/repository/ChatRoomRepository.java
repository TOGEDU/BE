package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


}