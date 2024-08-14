package passion.togedu.dto.DailyQuestion;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyQuestionRequestDto {
    private Integer questionId;
    private String text;
}
