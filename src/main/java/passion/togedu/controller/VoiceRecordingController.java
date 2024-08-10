package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.dto.sign.SignUpResponseDto;
import passion.togedu.dto.voiceRecording.VoiceRecordingRecordDto;
import passion.togedu.dto.voiceRecording.VoiceResponseDto;
import passion.togedu.service.VoiceRecordingService;
import java.io.IOException;
import java.util.List;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@RequestMapping("/api/voice")
@RestController
@RequiredArgsConstructor
public class VoiceRecordingController {

    private final VoiceRecordingService voiceRecordingService;

    @GetMapping
    public ResponseEntity<VoiceResponseDto> getVoiceRecordStatusAndSentences() {
        Integer id = getCurrentMemberId();
        VoiceResponseDto responseDto = voiceRecordingService.getVoiceRecordStatusAndSentences(id);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<SignUpResponseDto> addVoiceRecord(@RequestParam("id") Integer sentenceId,
                                                            @RequestParam("voiceRecord") MultipartFile voiceRecord) throws IOException {
        Integer id = getCurrentMemberId();
        SignUpResponseDto responseDto = voiceRecordingService.addRecording(id, sentenceId, voiceRecord);
        return ResponseEntity.ok(responseDto);
    }
}
