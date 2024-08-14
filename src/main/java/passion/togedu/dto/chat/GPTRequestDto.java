package passion.togedu.dto.chat;

import lombok.Data;
import passion.togedu.domain.Message;

import java.util.ArrayList;
import java.util.List;
@Data
public class GPTRequestDto {
    private String model;
    private List<Message> messages;

    public GPTRequestDto(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user",prompt));
    }
}
