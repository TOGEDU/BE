package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Parent;
import passion.togedu.domain.VoiceRecordingRecord;
import passion.togedu.domain.VoiceRecordingSentence;
import passion.togedu.dto.sign.SignUpResponseDto;
import passion.togedu.dto.voiceRecording.VoiceRecordingSentenceDto;
import passion.togedu.dto.voiceRecording.VoiceResponseDto;
import passion.togedu.repository.ParentRepository;
import passion.togedu.repository.VoiceRecordingRecordRepository;
import passion.togedu.repository.VoiceRecordingSentenceRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public SignUpResponseDto addRecording(Integer userId, Integer sentenceId, MultipartFile voiceRecord) throws IOException {

        Parent parent = parentRepository.findById(userId).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
        VoiceRecordingSentence voiceRecordingSentence = voiceRecordingSentenceRepository.findById(sentenceId).orElseThrow(() -> new RuntimeException("음성 녹음 문장을 찾을 수 없습니다."));

        if (voiceRecordingRecordRepository.existsByParentAndVoiceRecordingSentence(parent, voiceRecordingSentence)){
            throw new RuntimeException("이미 녹음된 문장입니다.");
        }

        String recordingUrl = s3UploadService.saveFile(voiceRecord);

        VoiceRecordingRecord recording = VoiceRecordingRecord.builder()
                .recordingUrl(recordingUrl)
                .parent(parent)
                .voiceRecordingSentence(voiceRecordingSentence)
                .build();

        voiceRecordingRecordRepository.save(recording);

        return SignUpResponseDto.builder()
                .success(Boolean.TRUE)
                .msg("녹음 성공")
                .build();
    }
}