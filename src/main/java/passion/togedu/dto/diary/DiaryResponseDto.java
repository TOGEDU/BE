package passion.togedu.dto.diary;

import lombok.*;
import passion.togedu.domain.Diary;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryResponseDto {
    private boolean status;
    private LocalDate date;
    private List<Diary> diaryList;
}
