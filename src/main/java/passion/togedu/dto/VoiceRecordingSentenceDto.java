package passion.togedu.dto;

import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceRecordingSentenceDto {
    private Integer id;
    private String sentenceText;
}
