package passion.togedu.dto.voiceRecording;

import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceRecordingSentenceDto {
    private Integer id;
    private String text;
}
