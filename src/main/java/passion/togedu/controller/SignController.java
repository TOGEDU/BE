package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import passion.togedu.dto.ParentVerificationResponseDTO;
import passion.togedu.service.SignService;

@RequestMapping("/api/sign")
@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignService signService;

    @GetMapping("/parent/verification") // 부모 회원 가입 - 본인 인증
    public ResponseEntity<ParentVerificationResponseDTO> verifyParent(@RequestParam("parentCode") String parentCode){
        ParentVerificationResponseDTO responseDTO = signService.verifyParent(parentCode);
        return ResponseEntity.ok(responseDTO);
    }

}
