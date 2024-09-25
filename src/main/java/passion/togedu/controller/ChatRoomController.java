package passion.togedu.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import passion.togedu.dto.chat.*;
import passion.togedu.jwt.SecurityUtil;
import passion.togedu.service.ChatRoomService;

import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //내용 요약 후 채팅방 생성
    @GetMapping("/chatroom")
    public ResponseEntity<ChatRoomResponseDto> chat(HttpServletRequest request, @RequestParam(name = "prompt") String prompt) {
        Integer id = getCurrentMemberId();
        try {
            String jwtToken = SecurityUtil.resolveToken(request);
            if (jwtToken == null) {
                throw new RuntimeException("사용자의 JWT 토큰 정보를 찾을 수 없습니다.");
            }
            ChatRoomResponseDto responseDto = chatRoomService.createRoom(id, prompt, jwtToken);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            log.error("Error adding chat message: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }


    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public ChatRoomRequestDto getAllRooms() {
        Integer id = getCurrentMemberId();
        return chatRoomService.findAllRooms(id);
    }

    //채팅방 내용조회
    @GetMapping("/chats")
    public ChatRoomResponseDto getAllMessage(@RequestParam("roomid")Integer roomId){
        Integer id = getCurrentMemberId();
        return chatRoomService.getAllMessage(roomId, id);
    }

}

