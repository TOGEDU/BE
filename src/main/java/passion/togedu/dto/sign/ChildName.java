package passion.togedu.dto.sign;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ChildName {
    // 부모 회원 가입 시 사용됨. 참고: ParentSignUpRequestDto
    private String name;
}
