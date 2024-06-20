package passion.togedu.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passion.togedu.dto.fcm.FCMNotificationRequestDto;

@Service
@RequiredArgsConstructor
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;

    public String sendNotificationByToken(FCMNotificationRequestDto requestDto){

        Notification notification = Notification.builder()
                .setTitle(requestDto.getTitle())
                .setBody(requestDto.getBody())
                .setImage(requestDto.getImage())
                .build();

        Message message = Message.builder()
                .setToken(requestDto.getFirebaseToken())
                .setNotification(notification)
                .build();

        try{
            firebaseMessaging.send(message);
            return "알림을 성공적으로 전송했습니다. FCM token: " + requestDto.getFirebaseToken();
        } catch (FirebaseMessagingException e){
            e.printStackTrace();
            return "알림 보내기를 실패하였습니다. FCM Token: " + requestDto.getFirebaseToken();
        }

    }




}
