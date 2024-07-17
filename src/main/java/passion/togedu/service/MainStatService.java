package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.domain.DailyQuestion;
import passion.togedu.dto.main.MainStatDto;
import passion.togedu.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainStatService {
/*
    private final DailyQuestionRecordRepository dailyQuestionRecordRepository;
    private final DailyQuestionRepository dailyQuestionRepository;
    private final VoiceRecordingRecordRepository voiceRecordingRecordRepository;
    private final VoiceRecordingSentenceRepository voiceRecordingSentenceRepository;
    private final DiaryRepository diaryRepository;

    public MainStatDto getMainStatDto(Integer userId) {
        //오늘의 질문 가져오기
        String question = findMissingDailyQuestion(userId);

        //오늘의 질문 연속 답변한 일수
        List<LocalDateTime> dates = dailyQuestionRecordRepository.findAllDatesByUserId(userId);
        Integer answerCount = calculateStreak(dates);

        //녹음 퍼센트 계산
        Integer recordPer = calRecordPer(userId);

        //일기 작성 개수 조회
        Integer diaryCount = diaryRepository.countAllDatesByUserId(userId);

        //모든 데이터 개수 조회
        Integer dataAll = countAllData(userId);

        // MainStatDto 객체 생성
        MainStatDto mainStatDto = MainStatDto.builder()
                .question(question)
                .answerCount(answerCount)
                .recordPer(recordPer)
                .diaryCount(diaryCount)
                .dataAll(dataAll)
                .build();

        //데이터 담겨있는지 테스트
        System.out.println("Question: " + mainStatDto.getQuestion());
        System.out.println("Answer Count: " + mainStatDto.getAnswerCount());
        System.out.println("Record Percentage: " + mainStatDto.getRecordPer());
        System.out.println("Diary Count: " + mainStatDto.getDiaryCount());
        System.out.println("Data All: " + mainStatDto.getDataAll());

        return mainStatDto;
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

    //Streak 계산
    private int calculateStreak(List<LocalDateTime> dates) {
        //데이터가 없을 경우
        if (dates == null || dates.isEmpty()) {
            return 0;
        }
        // 계산 기준
        LocalDate today = LocalDate.now();
        LocalDate currentDate = today; // 오늘 날짜로 시작
        int streak = 0;
        boolean isTodayAnswered = false;

        for (LocalDateTime dateTime : dates) {
            LocalDate date = dateTime.toLocalDate();

            if (date.equals(today)) {
                //오늘 답변한경우 streak추가
                streak++;
            } else if (date.equals(currentDate.minusDays(1))) {
                //전날 답변이 존재할경우 streak 추가 후 currentDate 변경
                streak++;
                currentDate = currentDate.minusDays(1);
            } else if (date.isBefore(currentDate.minusDays(1))) {
                //전날 답변이 존재하지 않는 경우 break 이후 return
                break;
            }
        }

        return streak;
    }
    private Integer calRecordPer(Integer userId){
        Integer recorded = voiceRecordingRecordRepository.countByUserId(userId);
        Integer sentences = voiceRecordingSentenceRepository.countTotalSentences();
        Integer recordPer = (sentences == 0) ? 0 : (recorded * 100) / sentences;
        //(recordedCount * 100) / totalSentences; 0개일리 없으면 나중에 수정!
        return recordPer;
    }
    //dataAll 계산
    private int countAllData(Integer userId) {
        Integer voiceRecordCount = voiceRecordingRecordRepository.countByUserId(userId);
        Integer questionRecordCount = dailyQuestionRecordRepository.countByUserId(userId);
        Integer diaryCount = diaryRepository.countByUserId(userId);

        // 전체 데이터 개수 합산
        return voiceRecordCount + questionRecordCount + diaryCount;
    }

    public List<MainCalendarDto> getMainCalendarDto(Integer userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        List<MainCalendarDto> calendar = new ArrayList<>();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            boolean hasDiary = diaryRepository.existsByParentIdAndDate(userId, date);
            boolean hasDailyQuestion = dailyQuestionRecordRepository.existsByParentIdAndDate(userId, date);
            boolean hasRecord = voiceRecordingRecordRepository.existsByParentIdAndDate(userId, date);

            MainCalendarDto calendarDto = MainCalendarDto.builder()
                    .date(date)
                    .diary(hasDiary)
                    .dailyQuestion(hasDailyQuestion)
                    .record(hasRecord)
                    .build();

            calendar.add(calendarDto);
        }

        return calendar;
    }*/
}
