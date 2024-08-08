package passion.togedu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.domain.ChatMessage;
import passion.togedu.domain.ChatRoom;
import passion.togedu.dto.chat.ChatMessageRequestDto;
import passion.togedu.dto.chat.ChatMessageResponseDto;
import passion.togedu.repository.ChatMessageRepository;
import passion.togedu.repository.ChatRoomRepository;
import passion.togedu.repository.ChildRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@RequiredArgsConstructor
@Service

public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;


    private static final String FASTAPI_URL = "http://127.0.0.1:8000/chat"; // Replace with your FastAPI server URL

    //채팅추가
    public ChatMessageResponseDto addMessage(ChatMessageRequestDto chatMessageRequestDto) {
        // ChatRoom ID를 이용해 ChatRoom을 검색
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequestDto.getChatRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        //ChatMessage 생성
        ChatMessage chatMessage = ChatMessage.builder()
                .message(chatMessageRequestDto.getMessage())
                .role(0) //0이 사용자
                .chatRoom(chatRoom)
                .build();
        chatMessageRepository.save(chatMessage);

        // FastAPI 서버로 채팅 메시지 전송 및 답변 받기
        String responseMessage = sendChatToFastApi(chatMessage);

        // FastAPI 서버의 답변을 새로운 메시지로 저장
        ChatMessage responseChatMessage = ChatMessage.builder()
                .message(responseMessage)
                .role(1) //1이 AI
                .chatRoom(chatRoom)
                .build();
        chatMessageRepository.save(responseChatMessage);

        return new ChatMessageResponseDto(chatMessage);
    }

    public String sendChatToFastApi(ChatMessage chatMessage) {
        try {
            // HTTP client 만들기
            HttpClient client = HttpClient.newHttpClient();

            // JSON 형태로 만들기
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();
            json.put("id", chatMessage.getId());
            json.put("message", chatMessage.getMessage());
            json.put("chat_room_id", chatMessage.getChatRoom().getId());

            // HTTP request 만들기
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FASTAPI_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
            // 보내고 답변받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 답변 예외처리
            if (response.statusCode() == 200) {
                ObjectNode responseJson = objectMapper.readValue(response.body(), ObjectNode.class);
                return responseJson.get("message").asText();
            } else {
                throw new RuntimeException("답변받기 실패, 상태코드: " + response.statusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("잘못된 메세지", e);
        }
    }
}
