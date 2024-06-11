package passion.togedu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInResponseDto {
    private Boolean success;
    private String msg;
    private String role;
    private String token;
}
