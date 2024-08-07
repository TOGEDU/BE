package passion.togedu.dto.gallery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    // 자녀 사진첩 조회할 때 각각의 사진에 두 가지의 데이터만 넣기 위해 사용됨.
    private LocalDate date;
    private String imgUrl;
}
