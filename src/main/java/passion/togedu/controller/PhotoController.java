package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passion.togedu.dto.gallery.GalleryResponseDto;
import passion.togedu.service.DiaryService;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class PhotoController {

    private final DiaryService diaryService;

    @GetMapping
    public ResponseEntity<GalleryResponseDto> getAllPhotos() {
        Integer id = getCurrentMemberId();
        GalleryResponseDto responseDto = diaryService.getAllDiaryImageUrls(id);
        return ResponseEntity.ok(responseDto);
    }
}
