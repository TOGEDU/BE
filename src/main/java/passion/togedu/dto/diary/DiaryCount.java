package passion.togedu.dto.diary;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DiaryCount {
    private LocalDate date;
    private Integer count;
}
