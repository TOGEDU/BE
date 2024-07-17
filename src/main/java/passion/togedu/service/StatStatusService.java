package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.dto.stat.StatStatusDto;
import passion.togedu.repository.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatStatusService {
    private final DailyQuestionRecordRepository dailyQuestionRecordRepository;
    private final DiaryRepository diaryRepository;
    List<StatStatusDto> weekCalendar = new ArrayList<>();

    public List<StatStatusDto> getStatStatusDto(Integer userId) {
        //오늘
        LocalDate now = LocalDate.now();
        // 현재 요일
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        // 해당 주의 월요일
        LocalDate startOfWeek = now.minusDays(dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue());

        for (LocalDate date = startOfWeek; !date.isAfter(now); date = date.plusDays(1)) {
            boolean hasDiary = diaryRepository.existsByParentIdAndDate(userId, date);
            boolean hasDailyQuestion = dailyQuestionRecordRepository.existsByParentIdAndDate(userId, date);
            boolean badge = hasDiary && hasDailyQuestion;
            StatStatusDto statStatusDto = StatStatusDto.builder()
                    .date(date)
                    .badge(badge)
                    .build();
            weekCalendar.add(statStatusDto);
        }

        return weekCalendar;
    }
}
