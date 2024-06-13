package passion.togedu.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInResponseDto {
    // 로그인 시 사용되는 dto
    private Boolean success;
    private String msg;
    private String role;
    private String grantType;
    private String token;
}
