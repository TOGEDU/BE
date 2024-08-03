package passion.togedu.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MypageParentResponseDto {
    // 부모 - 마이페이지 dto
    private String profileImage;
    private String name;
    private String email;
    private LocalTime pushNotificationTime;
    private Boolean pushStatus;
    private List<ChildIdAndName> childList;
}
