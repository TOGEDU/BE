package passion.togedu.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voiceRecordingSentence", cascade = CascadeType.ALL)
    private List<VoiceRecordingRecord> voiceRecordingRecordList;

}
