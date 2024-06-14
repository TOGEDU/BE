package passion.togedu.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import passion.togedu.domain.ChatMessage;
import passion.togedu.domain.ChatRoom;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private Integer id;
    private String message;
    private ChatRoom chatRoom;

    @Builder
    public ChatMessageRequestDto(Integer id, String message, ChatRoom chatRoom) {
        this.id = id;
        this.message = message;
        this.chatRoom = chatRoom;
    }

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .id(this.id)
                .message(this.message)
                .chatRoom(this.chatRoom)
                .build();
    }
}
