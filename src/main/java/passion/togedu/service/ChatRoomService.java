package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import passion.togedu.domain.*;
import passion.togedu.dto.chat.*;
import passion.togedu.repository.ChatMessageRepository;
import passion.togedu.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.ParentRepository;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ParentRepository parentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChildRepository childRepository;
    private final ParentChildService parentChildService;
    private final ChatMessageService chatMessageService;

    private static final String FASTAPI_URL = "http://127.0.0.1:8000"; // Replace with your FastAPI server URL
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.chat-url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    //채팅방 생성
    public ChatRoomResponseDto createRoom(Integer userId, String prompt, String jwtToken){
        GPTRequestDto request = new GPTRequestDto(model, prompt + "를 15글자 이하의 명사구로 요약해줘");
        // 답변 들고오기
        GPTResponseDto chatGPTResponse = template.postForObject(apiURL, request, GPTResponseDto.class);
        String summary = chatGPTResponse.getChoices().get(0).getMessage().getContent();
        // 첫문장과 요약가지고 채팅방 만들기
        Child child = childRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        //채팅방만들기
        ChatRoom chatRoom = ChatRoom.builder()
                .date(LocalDateTime.now())
                .summary(summary)
                .child(child)
                .build();
        chatRoomRepository.save(chatRoom);

        //처음 채팅 추가하기
        ChatMessage chatMessage = ChatMessage.builder()
                .message(prompt)
                .role(0) //0이 사용자
                .chatRoom(chatRoom)
                .build();

        // ChatMessage 저장
        chatMessageRepository.save(chatMessage);

        //처음 채팅에 대한 답변 얻기
        String responseMessage = chatMessageService.sendChatToFastApi(jwtToken, chatRoom.getId(), prompt);
        //String responseMessage = first + "의 답변이요";
        // FastAPI 서버의 답변을 새로운 메시지로 저장
        ChatMessage responseChatMessage = ChatMessage.builder()
                .message(responseMessage)
                .role(1) //1이 AI
                .chatRoom(chatRoom)
                .build();
        chatMessageRepository.save(responseChatMessage);

        //Response내용

        //부모 프로필 사진
        Parent parent = parentRepository.findById(parentChildService.getParentIdByChildId(userId)).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
        String profileImage = parent.getProfileImageUrl();
        //보낸메세지랑 받은메세지 묶어서
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(chatRoom.getId());

        //메세지를 DTO로 변환
        List<ChatMessageResponseDto> messageList = chatMessages.stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList());

        return new ChatRoomResponseDto(chatRoom.getId(), chatRoom.getDate(), profileImage, messageList);
    }

    //채팅방 조회
    public ChatRoomRequestDto findAllRooms(Integer childId) {
        List<ChatRoomDto> chatRoomList = chatRoomRepository.findByChildId(childId).stream()
                .map(ChatRoomDto::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list;
                }));

        return new ChatRoomRequestDto(chatRoomList);
    }

    //채팅 내용 전체 조회
    public ChatRoomResponseDto getAllMessage(Integer roomId, Integer userId) {

        ChatRoom chatRoom = chatRoomRepository.findByIdAndChild_Id(roomId, userId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
        //부모 프로필 사진
        Parent parent = parentRepository.findById(parentChildService.getParentIdByChildId(userId)).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
        String profileImage = parent.getProfileImageUrl();

        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(roomId);

        //메세지를 DTO로 변환
        List<ChatMessageResponseDto> messageList = chatMessages.stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list;
                }));

        return new ChatRoomResponseDto(chatRoom.getId(), chatRoom.getDate(), profileImage, messageList);
    }

}
