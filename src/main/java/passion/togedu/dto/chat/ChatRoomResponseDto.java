package passion.togedu.dto.chat;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Getter
public class ChatRoomResponseDto {
    //채팅 조회시, 보여줄 내용
    private Integer chatroomId;
    private String date; // String 타입으로 변경하여 날짜 포맷 적용
    private String profileImage;
    private List<ChatMessageResponseDto> messageList;


    public ChatRoomResponseDto(Integer chatroomId, LocalDateTime date, String profileImage, List<ChatMessageResponseDto> messageList) {
        this.chatroomId = chatroomId;
        this.date = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")); // 날짜 포맷 적용
        this.profileImage = profileImage;
        this.messageList = messageList;
    }


}
