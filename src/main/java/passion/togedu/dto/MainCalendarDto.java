package passion.togedu.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class MainCalendarDto {
    //달력 화면 클릭 시 보이는 정보
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime date;
    private boolean diary;
    private boolean dailyQuestion;
    private boolean record;
}
