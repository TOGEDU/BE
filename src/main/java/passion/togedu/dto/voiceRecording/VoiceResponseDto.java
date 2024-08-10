package passion.togedu.dto.voiceRecording;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoiceResponseDto {
    // 음성 기록 진행 현황 페이지 dto

    private int progressPercentage;
    private List<VoiceRecordingSentenceDto> sentenceList;

}
