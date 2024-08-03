package passion.togedu.dto.DailyQuestion;

import lombok.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyQuestionResponseDto {
    private Integer questionId;
    private LocalDate date;
    private String question;
    private String text;
}
