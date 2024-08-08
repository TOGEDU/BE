package passion.togedu.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParentSignUpRequestDto {
    // 부모 회원 가입 Request를 받을 때 쓰이는 dto
    private Integer parentId;
    private String name;
    private String email;
    private String password;
    private List<ChildName> childNameList;
    private LocalTime pushNotificationTime;
}
