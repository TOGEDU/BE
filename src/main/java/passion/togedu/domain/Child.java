package passion.togedu.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
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
    private LocalDateTime birthDate;

    @Column(length = 100)
    private String email;

    private Boolean pushStatus;

    @Column
    private LocalTime pushNotificationTime; //그냥 이렇게 해도 되는걸까요

    @Column(length = 50)
    private String password;

    @Column(length = 500)
    private String generatedTtsPath;

    @Column(length = 500)
    private String generatedChatbotPath;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
    private List<ChatRoom> chatRoomList;


}