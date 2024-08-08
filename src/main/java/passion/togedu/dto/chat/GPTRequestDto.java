package passion.togedu.dto.chat;

import lombok.Data;
import passion.togedu.domain.Summary;

import java.util.ArrayList;
import java.util.List;
@Data
public class GPTRequestDto {
    private String model;
    private Summary messages;

    public GPTRequestDto(String model, String prompt) {
        this.model = model;
        this.messages =  new Summary("user", prompt);
    }
}
