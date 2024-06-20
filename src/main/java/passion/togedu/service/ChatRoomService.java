package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.domain.ChatRoom;
import passion.togedu.dto.chat.ChatRoomDto;
import passion.togedu.repository.ChatRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    //채팅방 생성
    public ChatRoomDto createRoom(){
        ChatRoom chatRoom = ChatRoom.builder().build();
        chatRoomRepository.save(chatRoom);
        return new ChatRoomDto(chatRoom);
    }

    //채팅방 조회
    public List<ChatRoomDto> findAllRooms(){
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.toList());
    }

}
