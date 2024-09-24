package passion.togedu.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import passion.togedu.dto.chat.ChatMessageRequestDto;
import passion.togedu.dto.chat.ChatMessageResponseDto;
import passion.togedu.jwt.SecurityUtil;
import passion.togedu.service.ChatMessageService;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/message")
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    //채팅 추가하고 답변 받기
    @PostMapping("")
    public ResponseEntity<ChatMessageResponseDto> addMessage(HttpServletRequest request, @RequestBody ChatMessageRequestDto chatMessageRequestDto) {
        try {
            String jwtToken = SecurityUtil.resolveToken(request);
            if (jwtToken == null) {
                throw new RuntimeException("사용자의 JWT 토큰 정보를 찾을 수 없습니다.");
            }
            ChatMessageResponseDto responseDto = chatMessageService.addMessage(jwtToken, chatMessageRequestDto);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            log.error("Error adding chat message: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
