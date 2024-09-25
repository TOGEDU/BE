package passion.togedu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import passion.togedu.domain.ChatMessage;
import passion.togedu.domain.ChatRoom;
import passion.togedu.dto.chat.ChatMessageHistoryDto;
import passion.togedu.dto.chat.ChatMessageRequestDto;
import passion.togedu.dto.chat.ChatMessageResponseDto;
import passion.togedu.repository.ChatMessageRepository;
import passion.togedu.repository.ChatRoomRepository;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;


    private static final String FASTAPI_URL = "http://127.0.0.1:8000"; // Replace with your FastAPI server URL

    //채팅추가
    public ChatMessageResponseDto addMessage(String jwtToken, ChatMessageRequestDto chatMessageRequestDto) {
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
        String responseMessage = sendChatToFastApi(jwtToken, chatMessageRequestDto.getChatRoomId(), chatMessageRequestDto.getMessage());

        // FastAPI 서버의 답변을 새로운 메시지로 저장
        ChatMessage responseChatMessage = ChatMessage.builder()
                .message(responseMessage)
                .role(1) //1이 AI
                .chatRoom(chatRoom)
                .build();
        chatMessageRepository.save(responseChatMessage);

        return new ChatMessageResponseDto(responseChatMessage);
    }

    public String sendChatToFastApi(String jwtToken, Integer chatRoomId, String chatMessage) {
        try {
            // HTTP client 만들기
            HttpClient client = HttpClient.newHttpClient();

            // 채팅방 내역 가져오기
            List<ChatMessageHistoryDto> chatMessageHistoryDtoList = getChatHistory(chatRoomId);

            // 채팅 내역을 하나의 문자열로 변환
            StringBuilder chatHistoryBuilder = new StringBuilder();
            for (ChatMessageHistoryDto historyDto : chatMessageHistoryDtoList) {
                chatHistoryBuilder.append(historyDto.getRole()).append(": ").append(historyDto.getMessage()).append("\n");
            }
            String chatHistory = URLEncoder.encode(chatHistoryBuilder.toString(), StandardCharsets.UTF_8);

            // JSON 형태로 만들기
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode json = objectMapper.createObjectNode();
            json.put("message", chatMessage);

            String urlWithParams = FASTAPI_URL + "/chat?message=" + URLEncoder.encode(chatMessage, StandardCharsets.UTF_8)
                    + "&history=" + chatHistory;

            // HTTP request 만들기
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithParams))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + jwtToken)
                    .GET()
                    .build();

            log.info(request.toString()+ "\n" + request.uri());
            log.info(json.toString());
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
            // 예외 메시지와 스택 트레이스를 로그에 출력
            log.error("Exception occurred: " + e.getMessage(), e);
            throw new RuntimeException("잘못된 메세지", e);
        }
    }

    public List<ChatMessageHistoryDto> getChatHistory(Integer chatRoomId){
        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoomId);
        List<ChatMessageHistoryDto> chatMessageHistoryDtoList = new ArrayList<>();

        for (ChatMessage chatMessage : chatMessageList){
            ChatMessageHistoryDto dto = ChatMessageHistoryDto.builder()
                    .role(chatMessage.getRole() == 0 ? "자녀" : "부모")
                    .message(chatMessage.getMessage())
                    .build();
            chatMessageHistoryDtoList.add(dto);
        }
        return chatMessageHistoryDtoList;
    }
}
