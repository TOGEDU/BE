package passion.togedu.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoiceRecordingSentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String sentenceText;

    @OneToOne(mappedBy = "voiceRecordingSentence", cascade = CascadeType.ALL)
    private VoiceRecordingRecord voiceRecordingRecord;

}
