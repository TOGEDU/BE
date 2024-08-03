package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passion.togedu.service.DiaryService;
import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class PhotoController {

    private final DiaryService diaryService;

    @GetMapping
    public ResponseEntity<List<String>> getAllPhotos() {
        List<String> imageUrls = diaryService.getAllDiaryImageUrls();
        return ResponseEntity.ok(imageUrls);
    }
}
