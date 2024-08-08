package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.ChatMessage;
import passion.togedu.domain.ChatRoom;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    //채팅방 채팅 전부 조회
    List<ChatMessage> findByChatRoomId(Integer chatRoomId);

    List<ChatMessage> findAllByChatRoomId(Integer roomId);
}
