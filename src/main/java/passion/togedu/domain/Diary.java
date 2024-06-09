package passion.togedu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @Column(name = "diary_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_child_id")
    private ParentChild parentChild;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "text", length = 1000)
    private String text;

    @Column(name = "img_url", length = 500)
    private String imgUrl;

    // 기본 생성자
//    protected Diary() {
//    }

    // 필드를 설정할 수 있는 생성자
    public Diary(ParentChild parentChild, LocalDateTime date, String text, String imgUrl) {
        this.parentChild = parentChild;
        this.date = date;
        this.text = text;
        this.imgUrl = imgUrl;
    }

    // 연관관계 메서드(육아일기 기록/삭제할때 연관관계 일관되게 유지 -> 데이터 무결성)
    public void setParentChild(ParentChild parentChild) {
        this.parentChild = parentChild;
        if (parentChild != null && !parentChild.getDiaries().contains(this)) {
            parentChild.getDiaries().add(this);
        }
    }
}
