package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Parent;
import passion.togedu.domain.VoiceRecordingRecord;
import passion.togedu.domain.VoiceRecordingSentence;
import passion.togedu.dto.voiceRecording.VoiceRecordingRecordDto;
import passion.togedu.dto.voiceRecording.VoiceRecordingSentenceDto;
import passion.togedu.dto.voiceRecording.VoiceResponseDto;
import passion.togedu.repository.ParentRepository;
import passion.togedu.repository.VoiceRecordingRecordRepository;
import passion.togedu.repository.VoiceRecordingSentenceRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static java.time.LocalDate.*;

@Service
@RequiredArgsConstructor
public class VoiceRecordingService {

    private final VoiceRecordingRecordRepository voiceRecordingRecordRepository;

    private final VoiceRecordingSentenceRepository voiceRecordingSentenceRepository;

    private final S3UploadService s3UploadService;

    private final ParentRepository parentRepository;

    @Transactional
    public VoiceResponseDto getVoiceRecordStatusAndSentences(Integer id){
        Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));

        int totalSentenceCount = (int) voiceRecordingSentenceRepository.count(); // 전체 문장 수
        int recordedSentenceCount = parent.getVoiceRecordingRecordList().size(); // 이미 녹음한 문장 수

        // 퍼센트 계산
        // 퍼센트 계산 (반올림)
        int progressPercentage = 0;
        if (totalSentenceCount > 0) { // 0으로 나누는 것을 방지
            progressPercentage = (int) Math.round((recordedSentenceCount / (double) totalSentenceCount) * 100);
        }

        // 녹음 해야 하는 문장 리스트
        List<VoiceRecordingSentence> unRecordedSentenceList = voiceRecordingSentenceRepository.findUnrecordedSentencesByParentId(id);

        List<VoiceRecordingSentenceDto> sentenceDtoList = new ArrayList<>();

        for (VoiceRecordingSentence sentence: unRecordedSentenceList){
            VoiceRecordingSentenceDto sentenceDto = VoiceRecordingSentenceDto.builder()
                    .id(sentence.getId())
                    .text(sentence.getSentenceText())
                    .build();
            sentenceDtoList.add(sentenceDto);
        }

        return VoiceResponseDto.builder()
                .progressPercentage(progressPercentage)
                .sentenceList(sentenceDtoList)
                .build();
    }

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
                .text(sentence.getSentenceText())
                .build();
    }
}