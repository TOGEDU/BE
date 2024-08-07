package passion.togedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Parent;
import passion.togedu.domain.VoiceRecordingRecord;
import passion.togedu.domain.VoiceRecordingSentence;
import passion.togedu.dto.voiceRecording.VoiceRecordingRecordDto;
import passion.togedu.dto.voiceRecording.VoiceRecordingSentenceDto;
import passion.togedu.repository.ParentRepository;
import passion.togedu.repository.VoiceRecordingRecordRepository;
import passion.togedu.repository.VoiceRecordingSentenceRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static java.time.LocalDate.*;

@Service
public class VoiceRecordingService {

    @Autowired
    private VoiceRecordingRecordRepository voiceRecordingRecordRepository;

    @Autowired
    private VoiceRecordingSentenceRepository voiceRecordingSentenceRepository;

    @Autowired
    private S3UploadService s3UploadService;

    @Autowired
    private ParentRepository parentRepository;

    @Transactional
    public VoiceRecordingRecordDto addRecording(VoiceRecordingRecordDto recordingDTO, MultipartFile multipartFile) throws IOException {

        Parent parent = parentRepository.findById(recordingDTO.getParentId()).orElseThrow();
        VoiceRecordingSentence voiceRecordingSentence = voiceRecordingSentenceRepository.findById(recordingDTO.getSentenceId()).orElseThrow();

        if (voiceRecordingRecordRepository.existsByParentAndVoiceRecordingSentence(parent, voiceRecordingSentence)){
            throw new RuntimeException("이미 존재하는 녹음입니다.");
        }

        String recordingUrl = s3UploadService.saveFile(multipartFile);

        VoiceRecordingRecord recording = VoiceRecordingRecord.builder()
                .recordingUrl(recordingUrl)
                .parent(parent)
                .voiceRecordingSentence(voiceRecordingSentence)
                .date(LocalDateTime.from(now()))  //현재 날짜설정
                .build();

        VoiceRecordingRecord savedRecording = voiceRecordingRecordRepository.save(recording);

        return convertToDto(savedRecording);
    }

    public List<VoiceRecordingRecordDto> getRecordingsByParentId(Integer parentId) {
        return voiceRecordingRecordRepository.findByParentId(parentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public boolean hasRecordingOnDate(Integer parentId, String date) {
        LocalDate localDate = parse(date);
        return voiceRecordingRecordRepository.existsByParentIdAndDate(parentId, localDate);
    }

    public List<VoiceRecordingSentenceDto> getAllSentences() {
        return voiceRecordingSentenceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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