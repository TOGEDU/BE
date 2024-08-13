package passion.togedu.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryRecordResponseDto {
    // 육아일기 기록 조회할 때 사용되는 Dto
    private String childName;
    private Integer diaryId;
    private LocalDate date;
    private String title;
    private String image;
    private String content;
}
