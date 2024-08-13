package passion.togedu.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaryCheckAvailabilityResponseDto {
    // 육아일기 작성 전 작성 여부 조회하는 Dto
    private Integer parentChildId;
    private String name;
    private Boolean isHave;
}
