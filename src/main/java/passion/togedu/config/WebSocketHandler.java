package passion.togedu.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import passion.togedu.service.ChatRoomService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final ChatRoomService chatService;
    private static List<WebSocketSession> list = new ArrayList<>();
    //채팅방 생성시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
        System.out.println("클라이언트 접속: " + session);
    }

    //텍스트메세지 전송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)  throws Exception  {
        //payload는 전송되는 데이터 자체를 의미함.
        String payload = message.getPayload();
        System.out.println("payload: " + payload);
        for (WebSocketSession sess : list) {
            sess.sendMessage(message);
        }


    }
}
