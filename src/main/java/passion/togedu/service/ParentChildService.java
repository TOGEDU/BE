package passion.togedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Child;
import passion.togedu.domain.ParentChild;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.ParentChildRepository;

@Service
@Transactional
public class ParentChildService {

    @Autowired
    private ParentChildRepository parentChildRepository;

    @Autowired
    private ChildRepository childRepository;

    public ParentChild getParentChildById(int id) {
        return parentChildRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid parentChild ID: " + id));
    }

    public Integer getParentIdByChildId(Integer childId){
        Child child = childRepository.findById(childId).orElseThrow(() -> new RuntimeException("Child를 찾을 수 없습니다."));
        ParentChild parentChild = parentChildRepository.findByChild(child).orElseThrow(() -> new RuntimeException("ParentChild를 찾을 수 없습니다."));
        return parentChild.getParent().getId();
    }

}
