package passion.togedu.dto.DailyQuestion;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyQuestionDto {
    private Integer questionId;
    private String question;
    private String text;
}
