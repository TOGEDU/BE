package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Parent;
import passion.togedu.repository.ParentRepository;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParentService {
    private final ParentRepository parentRepository;

    @Transactional(readOnly = true)
    public Optional<Parent> loadUserById(Integer id){
        Optional<Parent> parent = parentRepository.findById(id);
        parent.ifPresent(p -> {
            Hibernate.initialize(p.getVoiceRecordingRecordList());
            Hibernate.initialize(p.getDailyQuestionRecordList());
            Hibernate.initialize(p.getParentChildList());
        });
        return parentRepository.findById(id);
    }

    public List<Parent> findParentsWithCurrentPushTimeAndStatus() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0); // 초와 나노초 제거
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        return parentRepository.findParentsWithCurrentPushTimeAndStatus(formattedTime);
    }

}
