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
    // 회원 가입 완료 후 보내는 dto
    private Boolean success;
    private String msg;
}
