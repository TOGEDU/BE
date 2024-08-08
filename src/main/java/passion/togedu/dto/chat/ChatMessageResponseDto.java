package passion.togedu.dto.chat;

import lombok.Getter;
import passion.togedu.domain.ChatMessage;

import java.time.format.DateTimeFormatter;

@Getter
public class ChatMessageResponseDto {
    //채팅 조회시, 채팅 하나당 보여줄 내용

    private String message;
    private Integer role;
    private String time;

    public ChatMessageResponseDto(ChatMessage message) {
        this.message = message.getMessage();
        this.role = message.getRole();
        //LocalDateTime -> String변환
        this.time = message.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
}
