package passion.togedu.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequestDto {
    // 로그인 시 사용되는 dto
    private String email;
    private String password;
    private String fcmToken;
}
