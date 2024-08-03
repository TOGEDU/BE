package passion.togedu.dto.sign;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentVerificationResponseDto {
    // 부모 본인 인증 시 사용되는 Dto
    private Boolean success;
    private String parentCode;
    private Integer parentId;
    private String name;
}
