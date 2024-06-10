package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Parent;
import passion.togedu.dto.ParentVerificationResponseDTO;
import passion.togedu.repository.ParentRepository;

@Service
@RequiredArgsConstructor
public class SignService {

    private final ParentRepository parentRepository;

    // 부모 회원 가입 - 본인 인증
    @Transactional
    public ParentVerificationResponseDTO verifyParent(String parentCode){
        if (parentRepository.findByParentCode(parentCode).isPresent()){ // 존재하는 경우
            return ParentVerificationResponseDTO.builder()
                    .success(Boolean.TRUE)
                    .parentCode(parentCode)
                    .parentId(parentRepository.findByParentCode(parentCode).get().getId())
                    .name(parentRepository.findByParentCode(parentCode).get().getName())
                    .build();
        }else{ // 존재하지 않는 경우
            return ParentVerificationResponseDTO.builder()
                    .success(Boolean.FALSE)
                    .parentCode(parentCode)
                    .parentId(null)
                    .name(null)
                    .build();
        }
    }

}
