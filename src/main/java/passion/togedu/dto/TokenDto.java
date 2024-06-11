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
    private String grantType;
    private String accessToken;
    private String role;
    private long accessTokenExpiresIn;
    private String refreshToken;
}
