package passion.togedu.dto;

import lombok.Getter;
import passion.togedu.domain.ChatMessage;

import java.time.format.DateTimeFormatter;

@Getter
public class ChatMessageResponseDto {
    private Integer id;
    private String message;
    private String time;

    public ChatMessageResponseDto(ChatMessage entity) {
        this.id = entity.getId();
        this.message = entity.getMessage();
        //LocalDateTime -> String변환
        this.time = entity.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
}
