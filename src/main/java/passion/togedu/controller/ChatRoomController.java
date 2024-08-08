package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import passion.togedu.domain.ChatMessage;
import passion.togedu.dto.chat.*;
import passion.togedu.service.ChatRoomService;
import java.util.List;

import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    //내용 요약 후 채팅방 생성
    @GetMapping("/chatroom")
    public ChatRoomRequestDto chat(@RequestParam(name = "prompt")String prompt){
        Integer id = getCurrentMemberId();
        //prompt가 처음 물어보는 질문
        //GPTRequestDto request = new GPTRequestDto(model, prompt+"를 15글자 이하의 명사구로 요약해줘");
        //답변 들고오기
        //GPTResponseDto chatGPTResponse =  template.postForObject(apiURL, request, GPTResponseDto.class);
        //String summary = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        //첫문장과 요약가지고 채팅방 만들기
        String summary = "요약문장입니다.";
        return chatRoomService.createRoom(id, prompt, summary);
    }

    // 채팅방 목록 조회
    @GetMapping("/rooms")
    public List<ChatRoomRequestDto> getAllRooms() {
        return chatRoomService.findAllRooms();
    }

    //채팅방 내용조회
    @GetMapping("/chats")
    public ChatRoomResponseDto getAllMessage(@RequestParam("roomid")Integer roomId){
        Integer id = getCurrentMemberId();
        return chatRoomService.getAllMessage(roomId, id);
    }

}

