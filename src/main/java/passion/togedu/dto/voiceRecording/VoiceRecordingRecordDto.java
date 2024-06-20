package passion.togedu.dto.voiceRecording;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceRecordingRecordDto {
    private Integer id;
    private String recordingUrl;
    private LocalDateTime date;
    private Integer parentId;
    private Integer sentenceId;
    private String sentenceText;
}
