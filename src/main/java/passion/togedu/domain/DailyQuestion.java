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
public class DailyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 500)
    private String questionText;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dailyQuestion", cascade = CascadeType.ALL)
    private List<DailyQuestionRecord> dailyQuestionRecord;

}
