package passion.togedu.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import passion.togedu.dto.mypage.ChildIdAndName;

import java.util.List;

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
    private List<ChildIdAndName> childList;
}
