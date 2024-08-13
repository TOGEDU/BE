package passion.togedu.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.YearMonth;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryCalendarResponseDto {
    // 육아일기 달력 페이지 Dto
    private YearMonth month;
    private List<DiaryCount> dateList;
}
