package passion.togedu.dto.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passion.togedu.domain.Diary;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequestDto {
    private int parentChildId;
    private LocalDate date;
    private String text;
    private String imgUrl;

    //Dto 추가
    public static DiaryRequestDto fromEntity(Diary diary) {
        return DiaryRequestDto.builder()
                .parentChildId(diary.getParentChild().getId())
                .date(diary.getDate())
                .text(diary.getContent())
                .imgUrl(diary.getImgUrl())
                .build();
    }
}
