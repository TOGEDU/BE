package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import passion.togedu.dto.chat.ChatRoomDto;
import passion.togedu.service.ChatRoomService;
import java.util.List;
@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    // 채팅방 생성
    @GetMapping
    public ChatRoomDto createRoom(@RequestParam String first) {
        return chatRoomService.createRoom();
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public List<ChatRoomDto> getAllRooms() {
        return chatRoomService.findAllRooms();
    }

}

