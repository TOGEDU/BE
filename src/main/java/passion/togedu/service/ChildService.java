package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Child;
import passion.togedu.repository.ChildRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;

    @Transactional
    public Optional<Child> loadUserById(Integer id){
        return childRepository.findById(id);
    }
}
