package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passion.togedu.dto.sign.*;
import passion.togedu.service.SignService;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberRole;

@RequestMapping("/api/sign")
@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @GetMapping("/parent/verification") // 부모 회원 가입 - 본인 인증
    public ResponseEntity<ParentVerificationResponseDto> verifyParent(@RequestParam("parentCode") String parentCode){
        ParentVerificationResponseDto responseDTO = signService.verifyParent(parentCode);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/emailduplicationcheck") // 부모, 자식 회원가입 - 이메일 중복 검사
    public ResponseEntity<EmailCheckResponseDto> checkEmailDuplicate(@RequestParam("id") Integer id, @RequestParam("email") String email){
        EmailCheckResponseDto responseDto = signService.checkEmailDuplicate(id, email);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/parent/sign-up") // 부모 회원 가입 - 회원 가입
    public ResponseEntity<SignUpResponseDto> parentSignUp(@RequestBody ParentSignUpRequestDto parentSignUpRequestDto){
        SignUpResponseDto responseDto = signService.parentSignUp(parentSignUpRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/child/check") // 자녀 회원가입 - 부모님 조회
    public ResponseEntity<ChildCheckResponseDto> checkChild(@RequestParam("uniqueCode") String uniqueCode){
        ChildCheckResponseDto responseDto = signService.checkChild(uniqueCode);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/child/sign-up") // 자녀 회원 가입 - 회원 가입
    public ResponseEntity<SignUpResponseDto> childSignUp(@RequestBody ChildSignUpRequestDto childSignUpRequestDto){
        SignUpResponseDto responseDto = signService.childSignUp(childSignUpRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/sign-in") // 로그인
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto signInRequestDto){
        SignInResponseDto responseDto = signService.signIn(signInRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout") // 로그아웃
    public ResponseEntity<SignUpResponseDto> logout(){
        Integer id = getCurrentMemberId();
        String role = getCurrentMemberRole();
        SignUpResponseDto responseDto = signService.logout(id, role);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/resign") // 자식 탈퇴
    public ResponseEntity<SignUpResponseDto> resign(){
        Integer id = getCurrentMemberId();
        String role = getCurrentMemberRole();
        SignUpResponseDto responseDto = signService.resign(id, role);
        return ResponseEntity.ok(responseDto);
    }



}
