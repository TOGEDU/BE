package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.domain.Child;
import passion.togedu.domain.DailyQuestion;
import passion.togedu.domain.DailyQuestionRecord;
import passion.togedu.domain.Parent;
import passion.togedu.dto.DailyQuestion.DailyQuestionDto;
import passion.togedu.dto.DailyQuestion.DailyQuestionRequestDto;
import passion.togedu.dto.DailyQuestion.DailyQuestionResponseDto;
import passion.togedu.repository.DailyQuestionRecordRepository;
import passion.togedu.repository.DailyQuestionRepository;
import passion.togedu.repository.ParentRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyQuestionService {
    private final DailyQuestionRecordRepository dailyQuestionRecordRepository;
    private final DailyQuestionRepository dailyQuestionRepository;
    private final ParentRepository parentRepository;

    public DailyQuestionDto showQuestion(Integer userId){
        DailyQuestionDto responseDto = new DailyQuestionDto();
        //답변데이터 개수
        Integer count = dailyQuestionRecordRepository.countByUserId(userId);
        //오늘 날짜에 기록 존재 여부
        boolean exists = dailyQuestionRecordRepository.existsByParentIdAndDate(userId,LocalDate.now());
        if (!exists){
            //오늘 질문 기록이 없을때, 오늘의 질문만 보여주기.
             String questionText = dailyQuestionRepository.findById(count+1)
                     .orElseThrow(() -> new RuntimeException("질문내용을 찾을 수 없습니다."))
                     .getQuestionText();
            responseDto.setQuestionId(count+1);
            responseDto.setQuestion(questionText);
            responseDto.setText(null);

        }else{
            //오늘 질문 기록이 있을때, 오늘의 질문과 답변 기록 보여주기
            DailyQuestionRecord record = dailyQuestionRecordRepository.findByUserIdAndQuestionId(userId,count);
            responseDto.setQuestionId(count);
            responseDto.setQuestion(record.getDailyQuestion().getQuestionText());
            responseDto.setText(record.getText());
        }
        return responseDto;
    }

    //DailyQuestion 찾기
    private String findMissingDailyQuestion(Integer userId) {
        // DailyQuestion의 데이터 개수 가져오기
        int totalQuestions = dailyQuestionRepository.findAll().size();
        for (int i = 1; i <= totalQuestions; i++) {
            boolean exists = dailyQuestionRecordRepository.existsByParentIdAndDailyQuestion_Id(userId, i);
            if (!exists) {
                // 누락된 DailyQuestion을 찾으면 해당 질문 반환
                return dailyQuestionRepository.findById(i)
                        .map(DailyQuestion::getQuestionText)
                        .orElse(null);
            }
        }
        return null;
    }

    public void addDailyQuestionRecord(Integer userId, DailyQuestionRequestDto dailyQuestionRequestDto) {
        boolean exists = dailyQuestionRecordRepository.existsByParentIdAndDailyQuestion_Id(userId,dailyQuestionRequestDto.getQuestionId());
        if (!exists){
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
            dailyQuestionRecordRepository.save(dailyQuestionRecord);}
        else {
            throw new RuntimeException("이미 질문의 답변이 존재합니다.");
        }

    }


    //목록 조회
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
        DailyQuestionRecord dailyQuestionRecord = dailyQuestionRecordRepository.findByUserIdAndQuestionId(id,dailyQuestionRequestDto.getQuestionId());
        dailyQuestionRecord.setText(dailyQuestionRequestDto.getText());
        dailyQuestionRecordRepository.save(dailyQuestionRecord);
    }
}
