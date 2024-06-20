package passion.togedu.dto.DailyQuestion;

import lombok.*;

import java.time.LocalTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DailyQuestionRequestDto {

    private Integer questionId;
    private String text;
}
