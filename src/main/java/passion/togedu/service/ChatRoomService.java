package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.domain.*;
import passion.togedu.dto.chat.ChatMessageRequestDto;
import passion.togedu.dto.chat.ChatMessageResponseDto;
import passion.togedu.dto.chat.ChatRoomRequestDto;
import passion.togedu.dto.chat.ChatRoomResponseDto;
import passion.togedu.repository.ChatMessageRepository;
import passion.togedu.repository.ChatRoomRepository;
import java.time.LocalDateTime;
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

    //채팅방 생성
    public ChatRoomResponseDto createRoom(Integer userId, String first, String summary){
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
                .message(first)
                .role(0) //0이 사용자
                .chatRoom(chatRoom)
                .build();

        // ChatMessage 저장
        chatMessageRepository.save(chatMessage);

        //처음 채팅에 대한 답변 얻기
        String responseMessage = chatMessageService.sendChatToFastApi(first);

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
    public List<ChatRoomRequestDto> findAllRooms(){
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomRequestDto::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.reverse(list);
                    return list;
                }));
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
                .collect(Collectors.toList());


        return new ChatRoomResponseDto(chatRoom.getId(), chatRoom.getDate(), profileImage, messageList);
    }

}
