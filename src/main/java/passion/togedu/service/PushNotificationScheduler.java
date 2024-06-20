package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Child;
import passion.togedu.domain.Parent;
import passion.togedu.dto.fcm.FCMNotificationRequestDto;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class PushNotificationScheduler {
    private final FCMNotificationService fcmNotificationService;
    private final ParentService parentService;
    private final ChildService childService;

    // 특정 시간에 스케줄링하여 실행되도록 설정
    @Scheduled(cron = "0 */30 * * * *") // 30분마다 함수를 실행
    public void checkAndSendPushNotification(){

        // 현재 시간의 시와 분에 맞는 pushNotificationTime, pushStatus가 true, 그리고 fcmToken이 null이 아닌 사용자
        List<Parent> parentList = parentService.findParentsWithCurrentPushTimeAndStatus();
        List<Child> childList = childService.findChildrenWithCurrentPushTimeAndStatus();

        for(Parent parent : parentList){
            FCMNotificationRequestDto requestDto = FCMNotificationRequestDto.builder()
                            .firebaseToken(parent.getFcmToken())
                            .title("기록할 시간입니다!")
                            .body("자녀를 위해 자신을 기록해주세요!")
                            .image("https://togedubucket.s3.ap-northeast-2.amazonaws.com/%EB%94%94%EC%8A%A4%EC%BD%94%EB%93%9C+%EB%B0%B0%EA%B2%BD%ED%99%94%EB%A9%B4.png")
                            .build();
            fcmNotificationService.sendNotificationByToken(requestDto);
        }

        for(Child child : childList){
            FCMNotificationRequestDto requestDto = FCMNotificationRequestDto.builder()
                    .firebaseToken(child.getFcmToken())
                    .title("오늘 하루도 부모님과 채팅해 보세요!")
                    .body("부모님께 궁금한 것을 물어보세요.")
                    .image("https://togedubucket.s3.ap-northeast-2.amazonaws.com/togedu_logo.png")
                    .build();
            fcmNotificationService.sendNotificationByToken(requestDto);
        }

    }

}
