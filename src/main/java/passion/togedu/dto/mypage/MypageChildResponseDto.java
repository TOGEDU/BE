package passion.togedu.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MypageChildResponseDto {
    // 자녀 - 마이페이지 dto
    private String name;
    private String email;
    private LocalTime pushNotificationTime;
    private Boolean pushStatus;
}
