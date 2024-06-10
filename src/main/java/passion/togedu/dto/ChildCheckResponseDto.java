package passion.togedu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildCheckResponseDto {
    // 자식 회원 가입 - 부모님 조회
    private Boolean success;
    private String uniqueCode;
    private Integer childId;
}
