package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.domain.Child;
import passion.togedu.domain.DailyQuestion;
import passion.togedu.domain.DailyQuestionRecord;
import passion.togedu.domain.Parent;
import passion.togedu.dto.DailyQuestion.DailyQuestionRequestDto;
import passion.togedu.dto.DailyQuestion.DailyQuestionResponseDto;
import passion.togedu.repository.DailyQuestionRecordRepository;
import passion.togedu.repository.DailyQuestionRepository;
import passion.togedu.repository.ParentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DailyQuestionService {
    private final DailyQuestionRecordRepository dailyQuestionRecordRepository;
    private final DailyQuestionRepository dailyQuestionRepository;
    private final ParentRepository parentRepository;

    public void addDailyQuestionRecord(Integer userId, DailyQuestionRequestDto dailyQuestionRequestDto) {
        Parent parent = parentRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 질문 조회
        DailyQuestion dailyQuestion = dailyQuestionRepository.findById(dailyQuestionRequestDto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));

        // 새로운 질문 기록 생성
        DailyQuestionRecord dailyQuestionRecord = DailyQuestionRecord.builder()
                .parent(parent)
                .dailyQuestion(dailyQuestion)
                .text(dailyQuestionRequestDto.getText())
                .date(LocalDateTime.now())
                .build();
        dailyQuestionRecordRepository.save(dailyQuestionRecord);
    }

    public List<DailyQuestionResponseDto> getDailyQuestionListDto(Integer userId) {
        //Record의 모든 데이터를 List로 반환
        List<DailyQuestionRecord> records = dailyQuestionRecordRepository.findAllByUserId(userId);
        //반환할 리스트
        List<DailyQuestionResponseDto> dailyQuestionList = new ArrayList<>();

        //List를 전부 돌며 DTO 생성
        for (DailyQuestionRecord record : records) {
            DailyQuestionResponseDto responseDto = new DailyQuestionResponseDto();
            responseDto.setQuestionId(record.getDailyQuestion().getId());
            responseDto.setDate(record.getDate().toLocalDate());
            responseDto.setQuestion(record.getDailyQuestion().getQuestionText());
            responseDto.setText(record.getText());
            //List에 추가
            dailyQuestionList.add(responseDto);
        }
        //List 반환
        return dailyQuestionList;
    }

    public void changeText(Integer id, DailyQuestionRequestDto dailyQuestionRequestDto) {
        DailyQuestionRecord dailyQuestionRecord = dailyQuestionRecordRepository.findByUserIdAndQuestionId(id,dailyQuestionRequestDto.getQuestionId()).orElseThrow(() -> new RuntimeException("기록을 찾을 수 없습니다."));
        dailyQuestionRecord.setText(dailyQuestionRequestDto.getText());
        dailyQuestionRecordRepository.save(dailyQuestionRecord);
    }
}
