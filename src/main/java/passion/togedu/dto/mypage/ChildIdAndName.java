package passion.togedu.dto.mypage;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ChildIdAndName {
    // 마이페이지 자녀 리스트 사용하는 부분 참고: MypageParentResponseDto
    private Integer childId;
    private String name;
}
