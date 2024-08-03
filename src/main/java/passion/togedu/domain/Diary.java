package passion.togedu.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "diary")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_child_id")
    @JsonBackReference
    private ParentChild parentChild;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "text", length = 1000)
    private String text;

    @Column(name = "img_url", length = 500)
    private String imgUrl;
}
