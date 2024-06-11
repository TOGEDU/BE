package passion.togedu.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 500)
    private String profileImageUrl;

    private Boolean pushStatus;

    private LocalTime pushNotificationTime;

    @Column(length = 500)
    private String password;

    @Column(length = 50)
    private String parentCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<VoiceRecordingRecord> voiceRecordingRecordList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<DailyQuestionRecord> dailyQuestionRecordList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<ParentChild> parentChildList;
}
