package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.dto.mypage.ChildIdAndName;
import passion.togedu.service.MypageService;

import java.io.IOException;
import java.time.LocalTime;

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

    @PutMapping("/push-time")
    public ResponseEntity<String> changePushNotificationTime(@RequestParam("pushNotificationTime") LocalTime pushNotificationTime){
        Integer id = getCurrentMemberId();
        String role = getCurrentMemberRole();
        mypageService.changePushNotificationTime(id, role, pushNotificationTime);
        return ResponseEntity.ok("알림 시간 변경 완료");
    }

    @PutMapping("/child")
    public ResponseEntity<String> changeChildName(@RequestBody ChildIdAndName child){
        mypageService.changeChildName(child);
        return ResponseEntity.ok("자녀 이름 변경 완료");
    }

    @PostMapping("/child")
    public ResponseEntity<String> addChild(@RequestParam("name") String childName){
        Integer id = getCurrentMemberId();
        mypageService.addChild(id, childName);
        return ResponseEntity.ok("자녀 추가 완료");
    }

    @PutMapping("/push-status")
    public ResponseEntity<String> changePushNotificationStatus(@RequestParam("pushStatus") Boolean pushStatus){
        Integer id = getCurrentMemberId();
        String role = getCurrentMemberRole();
        mypageService.changePushNotificationStatus(id, role, pushStatus);
        return ResponseEntity.ok("푸시 알림 상태 변경 완료");
    }

}
