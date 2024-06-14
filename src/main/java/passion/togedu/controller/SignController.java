package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passion.togedu.dto.sign.*;
import passion.togedu.service.SignService;

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




}
