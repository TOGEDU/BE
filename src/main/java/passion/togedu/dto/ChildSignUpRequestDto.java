package passion.togedu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChildSignUpRequestDto {
    // 자식 회원 가입 Request를 받을 때 쓰이는 dto
    private Integer childId;
    private String name;

    private LocalDate birthDate;
    private String email;
    private String password;
}
