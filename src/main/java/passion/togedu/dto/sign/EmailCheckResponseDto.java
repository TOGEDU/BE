package passion.togedu.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCheckResponseDto {
    // 이메일 중복 검사 후 보내는 dto
    private Boolean success;
    private Integer id;
    private String msg;
}
