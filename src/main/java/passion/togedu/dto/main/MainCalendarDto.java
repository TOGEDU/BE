package passion.togedu.dto.main;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MainCalendarDto {
    //달력 화면 클릭 시 보이는 정보
    private LocalDate date;
    private boolean diary;
    private boolean dailyQuestion;
    private boolean record;
}
