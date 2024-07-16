package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Child;
import passion.togedu.repository.ChildRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;

    @Transactional
    public Optional<Child> loadUserById(Integer id){
        return childRepository.findById(id);
    }

    @Transactional
    public List<Child> findChildrenWithCurrentPushTimeAndStatus(){
        LocalTime now = LocalTime.now().withSecond(0).withNano(0); // 초와 나노초 제거
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        return childRepository.findChildrenWithCurrentPushTimeAndStatus(formattedTime);
    }

    @Transactional
    public List<Child> findChildrenWithBirthdayToday(){
        return childRepository.findChildrenWithBirthdayToday();
    }
}
