package passion.togedu.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "parent_child")
public class ParentChild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_child_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @Column(name = "unique_code")
    private int uniqueCode;

    @OneToMany(mappedBy = "parentChild", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries = new ArrayList<>();



    // 기본 생성자
//    protected ParentChild() {
//    }

    // 필드를 설정할 수 있는 생성자
    public ParentChild(Parent parent, Child child, int uniqueCode) {
        this.parent = parent;
        this.child = child;
        this.uniqueCode = uniqueCode;
    }

    // 연관관계 메서드
    public void addDiary(Diary diary) {
        diaries.add(diary);
        diary.setParentChild(this);
    }

    public void removeDiary(Diary diary) {
        diaries.remove(diary);
        diary.setParentChild(null);
    }
}

