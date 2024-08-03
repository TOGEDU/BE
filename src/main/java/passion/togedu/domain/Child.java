package passion.togedu.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column
    private LocalDate birthDate;

    @Column(length = 100)
    private String email;

    private Boolean pushStatus;

    @Column
    private LocalTime pushNotificationTime; //그냥 이렇게 해도 되는걸까요

    @Column(length = 500)
    private String password;

    @Column(length = 500)
    private String generatedTtsPath;

    @Column(length = 500)
    private String generatedChatbotPath;

    @Column(length=200)
    private String fcmToken; // FCM 토큰 필드

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
    private List<ChatRoom> chatRoomList;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "child")
    private ParentChild parentChild;

}