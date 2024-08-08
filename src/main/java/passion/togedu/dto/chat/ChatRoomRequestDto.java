package passion.togedu.dto.chat;

import lombok.Getter;
import passion.togedu.domain.ChatRoom;

import java.time.format.DateTimeFormatter;

@Getter
public class ChatRoomRequestDto {
    //채팅방 생성 시, 필요한 내용
    private Integer id; //chatroomId
    private String date;
    private String summary;

    public ChatRoomRequestDto(ChatRoom chatRoom){
        this.id = chatRoom.getId();
        //LocalDateTime -> String변환
        this.date = chatRoom.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.summary = chatRoom.getSummary();
    }
}
