package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passion.togedu.dto.VoiceRecordingRecordDto;
import passion.togedu.dto.VoiceRecordingSentenceDto;
import passion.togedu.service.VoiceRecordingService;

import java.util.List;

@RequestMapping("/api/tts")
@RestController
@RequiredArgsConstructor
public class VoiceRecordingController {

    private final VoiceRecordingService voiceRecordingService;

    @GetMapping
    public ResponseEntity<List<VoiceRecordingSentenceDto>> getAllSentences() {
        List<VoiceRecordingSentenceDto> sentences = voiceRecordingService.getAllSentences();
        return ResponseEntity.ok(sentences);
    }

    @PostMapping
    public ResponseEntity<VoiceRecordingRecordDto> addRecording(@RequestBody VoiceRecordingRecordDto recordingDTO) {
        VoiceRecordingRecordDto savedRecording = voiceRecordingService.addRecording(recordingDTO);
        return ResponseEntity.ok(savedRecording);
    }

    @GetMapping("/list")
    public ResponseEntity<List<VoiceRecordingRecordDto>> getAllRecordings() {
        List<VoiceRecordingRecordDto> recordings = voiceRecordingService.getAllRecordings();
        return ResponseEntity.ok(recordings);
    }

    @GetMapping("/record")
    public ResponseEntity<VoiceRecordingRecordDto> getRecordingById(@RequestParam Integer id) {
        VoiceRecordingRecordDto recording = voiceRecordingService.getRecordingById(id);
        return ResponseEntity.ok(recording);
    }
}
