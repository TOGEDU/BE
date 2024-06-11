package passion.togedu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto {
    // 토큰을 생성할 때 사용되는 dto
    private String grantType;
    private String accessToken;
    private String role;
    private long accessTokenExpiresIn;
    private String refreshToken;
}
