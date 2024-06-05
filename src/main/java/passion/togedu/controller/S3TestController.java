package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Image;
import passion.togedu.repository.ImageRepository;
import passion.togedu.service.S3UploadService;

import java.io.IOException;

@RequestMapping("/s3")
@RestController
@RequiredArgsConstructor
public class S3TestController {
    // S3 테스트용 - 나중에 지워야 함

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


}
