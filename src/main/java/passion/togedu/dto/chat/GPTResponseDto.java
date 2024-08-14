package passion.togedu.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passion.togedu.domain.ChatMessage;
import passion.togedu.domain.Message;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GPTResponseDto {
    private List<Choice> choices;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;

    }
}
