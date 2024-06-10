package passion.togedu.dto;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentVerificationResponseDTO {
    // 부모 본인 인증 시 사용되는 DTO
    private Boolean success;
    private String parentCode;
    private Integer parentId;
    private String name;
}
