package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.dto.mypage.MypageChildResponseDto;
import passion.togedu.dto.mypage.MypageParentResponseDto;
import passion.togedu.service.MypageService;

import java.io.IOException;

import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberRole;

@RequestMapping("/api/mypage")
@RestController
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("")
    public ResponseEntity<?> getMypage(){
        Integer id = getCurrentMemberId();
        String role = getCurrentMemberRole();

        Object responseDto;

        if (role.equals("Parent")){
            responseDto = mypageService.getParentMypage(id);
        } else if (role.equals("Child")) {
            responseDto = mypageService.getChildMypage(id);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 역할이 잘못 되었습니다.");
        }
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/profile-image")
    public ResponseEntity<String> changeProfileImage(@RequestParam("profileImage") MultipartFile image) throws IOException {
        Integer id = getCurrentMemberId();
        mypageService.changeProfileImage(id, image);
        return ResponseEntity.ok("프로필 사진 변경 완료");
    }

}
