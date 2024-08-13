package passion.togedu.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponseDto {
    // 회원 가입 완료 후 보내는 dto, 음성 기록에도 사용되는 dto, 육아일기 작성 및 수정에도 사용되는 dto
    private Boolean success;
    private String msg;
}
