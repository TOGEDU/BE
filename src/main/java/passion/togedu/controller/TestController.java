package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Image;
import passion.togedu.repository.ImageRepository;
import passion.togedu.service.S3UploadService;

import java.io.IOException;

import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberRole;

@RequestMapping("/s3")
@RestController
@RequiredArgsConstructor
public class TestController {
    // 테스트용 - 나중에 지워야 함

    private final S3UploadService s3UploadService;
    private final ImageRepository imageRepository;

    @PostMapping("/image")
    public ResponseEntity<String> editProfile(@RequestParam("message") String message,
                                              @RequestParam("image") MultipartFile imageurl) throws IOException {


        String imgUrl = s3UploadService.saveFile(imageurl);

        Image image = Image.builder()
                .image(imgUrl)
                .build();

        imageRepository.save(image);

        return ResponseEntity.ok(message +  "이미지 저장 완료");

    }

    @GetMapping("/check-id") // getCurrentMemberId() 활용 방법
    public ResponseEntity<Integer> checkUserId(){
        Integer id = getCurrentMemberId();
        return ResponseEntity.ok(id);
    }

    @GetMapping("/check-role") // getCurrentMemberRole() 활용 방법
    public ResponseEntity<String> checkUserRole(){
        String role = getCurrentMemberRole();
        return ResponseEntity.ok(role);
    }


}
