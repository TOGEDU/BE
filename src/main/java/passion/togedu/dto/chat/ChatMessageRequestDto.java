package passion.togedu.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import passion.togedu.domain.ChatMessage;
import passion.togedu.domain.ChatRoom;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
    //채팅 추가 시, 필요한 내용
    private String message;
    private Integer chatRoomId;

    @Builder
    public ChatMessageRequestDto( String message,Integer chatRoomId) {
        this.message = message;
        this.chatRoomId = chatRoomId;
    }
}
