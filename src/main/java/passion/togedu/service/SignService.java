package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Child;
import passion.togedu.domain.Parent;
import passion.togedu.domain.ParentChild;
import passion.togedu.dto.sign.*;
import passion.togedu.jwt.TokenProvider;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.ParentChildRepository;
import passion.togedu.repository.ParentRepository;

import java.security.SecureRandom;
import java.sql.Time;
import java.util.Calendar;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SignService {

    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final ParentChildRepository parentChildRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Random RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    // 부모 회원 가입 - 본인 인증
    @Transactional
    public ParentVerificationResponseDto verifyParent(String parentCode){
        if (parentRepository.findByParentCode(parentCode).isPresent()){ // 존재하는 경우
            return ParentVerificationResponseDto.builder()
                    .success(Boolean.TRUE)
                    .parentCode(parentCode)
                    .parentId(parentRepository.findByParentCode(parentCode).get().getId())
                    .name(parentRepository.findByParentCode(parentCode).get().getName())
                    .build();
        }else{ // 존재하지 않는 경우
            return ParentVerificationResponseDto.builder()
                    .success(Boolean.FALSE)
                    .parentCode(parentCode)
                    .parentId(null)
                    .name(null)
                    .build();
        }
    }

    @Transactional
    public SignUpResponseDto parentSignUp(ParentSignUpRequestDto parentSignUpRequestDto){
        if (parentRepository.existsById(parentSignUpRequestDto.getParentId())){
            // 부모 데이터 추가
            Parent parent = parentRepository.findById(parentSignUpRequestDto.getParentId()).orElseThrow();
            parent.setEmail(parentSignUpRequestDto.getEmail());
            parent.setPassword(passwordEncoder.encode(parentSignUpRequestDto.getPassword()));
            parent.setPushNotificationTime(parentSignUpRequestDto.getPushNotificationTime());
            parent.setPushStatus(Boolean.TRUE);
            parentRepository.save(parent);

            // Child & parentChild 생성
            for (ChildName childName: parentSignUpRequestDto.getChildNameList()) {
                Child child = Child.builder()
                        .name(childName.getName())
                        .build();
                childRepository.save(child);

                ParentChild parentChild = ParentChild.builder()
                        .parent(parent)
                        .child(child)
                        .uniqueCode(generateUniqueString())
                        .build();

                parentChildRepository.save(parentChild);
            }

            return SignUpResponseDto.builder()
                    .success(Boolean.TRUE)
                    .msg("부모 회원가입 성공")
                    .build();

        }else{
            throw new RuntimeException("DB에 없는 사용자입니다.");
        }
    }

    @Transactional // 자식 고유 코드 생성
    public String generateUniqueString() {
        String uniqueString;
        do {
            uniqueString = generateRandomString();
        } while (parentChildRepository.existsByUniqueCode(uniqueString));
        return uniqueString;
    }

    private String generateRandomString() { // 자식 고유 코드 생성
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Transactional
    public ChildCheckResponseDto checkChild(String uniqueCode){
        if (parentChildRepository.findByUniqueCode(uniqueCode).isPresent()){ // 존재하는 경우
            return ChildCheckResponseDto.builder()
                    .success(Boolean.TRUE)
                    .uniqueCode(uniqueCode)
                    .childId(parentChildRepository.findByUniqueCode(uniqueCode).get().getChild().getId())
                    .build();
        }else{ // 존재하지 않는 경우
            return ChildCheckResponseDto.builder()
                    .success(Boolean.FALSE)
                    .uniqueCode(uniqueCode)
                    .childId(null)
                    .build();
        }
    }

    @Transactional
    public SignUpResponseDto childSignUp(ChildSignUpRequestDto childSignUpRequestDto){
        Child child = childRepository.findById(childSignUpRequestDto.getChildId()).orElseThrow();
        child.setName(childSignUpRequestDto.getName());
        child.setBirthDate(childSignUpRequestDto.getBirthDate());
        child.setEmail(childSignUpRequestDto.getEmail());
        child.setPassword(passwordEncoder.encode(childSignUpRequestDto.getPassword()));
        child.setPushStatus(Boolean.FALSE);

        // 푸시 알림 시간을 임의로 15시로 설정
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Time pushNotificationTime = new Time(calendar.getTimeInMillis());

        child.setPushNotificationTime(pushNotificationTime.toLocalTime());

        childRepository.save(child);

        return SignUpResponseDto.builder()
                .success(Boolean.TRUE)
                .msg("자식 회원가입 성공")
                .build();
    }

    @Transactional // 로그인 시도한 ID / PW String
    public SignInResponseDto signIn(SignInRequestDto requestDto){
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 5. 토큰 발급
        return SignInResponseDto.builder()
                .success(Boolean.TRUE)
                .msg("로그인 성공")
                .role(tokenDto.getRole())
                .grantType(tokenDto.getGrantType())
                .token(tokenDto.getAccessToken())
                .build();
    }

}
