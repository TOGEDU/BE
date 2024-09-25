package passion.togedu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageHistoryDto {
    // 채팅 메시지 보낼 때 채팅방 내역 전송용 dto
    private String role; // 자녀 또는 부모
    private String message;
}
