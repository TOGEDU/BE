package passion.togedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.ParentChild;
import passion.togedu.repository.ParentChildRepository;

@Service
@Transactional
public class ParentChildService {

    @Autowired
    private ParentChildRepository parentChildRepository;

    public ParentChild getParentChildById(int id) {
        return parentChildRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid parentChild ID: " + id));
    }
}
