package passion.togedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Parent;
import passion.togedu.domain.VoiceRecordingRecord;
import passion.togedu.domain.VoiceRecordingSentence;
import passion.togedu.dto.VoiceRecordingRecordDto;
import passion.togedu.dto.VoiceRecordingSentenceDto;
import passion.togedu.repository.VoiceRecordingRecordRepository;
import passion.togedu.repository.VoiceRecordingSentenceRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoiceRecordingService {

    @Autowired
    private VoiceRecordingRecordRepository voiceRecordingRecordRepository;

    @Autowired
    private VoiceRecordingSentenceRepository voiceRecordingSentenceRepository;

    @Autowired
    private S3UploadService s3UploadService;

    @Transactional
    public VoiceRecordingRecordDto addRecording(VoiceRecordingRecordDto recordingDTO, MultipartFile multipartFile) throws IOException {
        // Save file to S3 and get the URL
        String recordingUrl = s3UploadService.saveFile(multipartFile);

        // Create VoiceRecordingRecord entity
        VoiceRecordingRecord recording = VoiceRecordingRecord.builder()
                .recordingUrl(recordingUrl)
                .parent(Parent.builder().id(recordingDTO.getParentId()).build())
                .voiceRecordingSentence(VoiceRecordingSentence.builder().id(recordingDTO.getSentenceId()).build())
                .build();

        // Save VoiceRecordingRecord entity to database
        VoiceRecordingRecord savedRecording = voiceRecordingRecordRepository.save(recording);

        return convertToDto(savedRecording);
    }

    public List<VoiceRecordingRecordDto> getAllRecordings() {
        return voiceRecordingRecordRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<VoiceRecordingRecordDto> getRecordingsByParentId(Integer parentId) {
        return voiceRecordingRecordRepository.findByParentId(parentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VoiceRecordingRecordDto getRecordingById(Integer id) {
        return voiceRecordingRecordRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    public List<VoiceRecordingSentenceDto> getAllSentences() {
        return voiceRecordingSentenceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public VoiceRecordingSentenceDto getSentenceById(Integer id) {
        return voiceRecordingSentenceRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    private VoiceRecordingRecordDto convertToDto(VoiceRecordingRecord recording) {
        return VoiceRecordingRecordDto.builder()
                .id(recording.getId())
                .recordingUrl(recording.getRecordingUrl())
                .date(recording.getDate())
                .parentId(recording.getParent().getId())
                .sentenceId(recording.getVoiceRecordingSentence().getId())
                .sentenceText(recording.getVoiceRecordingSentence().getSentenceText())
                .build();
    }

    private VoiceRecordingSentenceDto convertToDto(VoiceRecordingSentence sentence) {
        return VoiceRecordingSentenceDto.builder()
                .id(sentence.getId())
                .sentenceText(sentence.getSentenceText())
                .build();
    }
}