package passion.togedu.dto.gallery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GalleryResponseDto {
    // 자녀 사집첩 조회 Dto
    private List<Photo> photoList;
}
