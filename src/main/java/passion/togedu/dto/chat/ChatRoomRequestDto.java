package passion.togedu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import passion.togedu.dto.gallery.Photo;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequestDto {
    private List<ChatRoomDto> chatList;
}
