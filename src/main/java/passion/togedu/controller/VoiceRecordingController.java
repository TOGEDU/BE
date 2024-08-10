package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.dto.voiceRecording.VoiceRecordingRecordDto;
import passion.togedu.dto.voiceRecording.VoiceRecordingSentenceDto;
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

    @PostMapping("/record")
    public ResponseEntity<VoiceRecordingRecordDto> addRecording(
            @RequestPart("recordingDTO") VoiceRecordingRecordDto recordingDTO,
            @RequestPart("file") MultipartFile file) throws IOException {
        VoiceRecordingRecordDto savedRecording = voiceRecordingService.addRecording(recordingDTO, file);
        return ResponseEntity.ok(savedRecording);
    }

    @GetMapping("/records")
    public ResponseEntity<List<VoiceRecordingRecordDto>> getRecordingsByParentId(@RequestParam Integer parentId) {
        List<VoiceRecordingRecordDto> recordings = voiceRecordingService.getRecordingsByParentId(parentId);
        return ResponseEntity.ok(recordings);
    }

    @GetMapping("/records/date")
    public ResponseEntity<Boolean> hasRecordingOnDate(
            @RequestParam Integer parentId,
            @RequestParam String date) { // "yyyy-MM-dd" 형식의 날짜
        boolean hasRecording = voiceRecordingService.hasRecordingOnDate(parentId, date);
        return ResponseEntity.ok(hasRecording);
    }
}
