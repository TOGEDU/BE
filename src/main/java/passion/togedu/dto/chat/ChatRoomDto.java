package passion.togedu.dto.chat;

import lombok.Getter;
import passion.togedu.domain.ChatRoom;
import java.time.format.DateTimeFormatter;

@Getter
public class ChatRoomDto {
    private Integer id;
    private String date;
    private String summary;

    public ChatRoomDto(ChatRoom entity){
        this.id = entity.getId();
        //LocalDateTime -> String변환
        this.date = entity.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.summary = "";
    }
}
