package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Child;
import passion.togedu.domain.Parent;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.ParentRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         if (parentRepository.existsByEmail(username)) { // 이메일이 부모에서 발견될 경우
             return parentRepository.findByEmail(username)
                     .map(this::createParentUserDetails)
                     .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
         }

        if (childRepository.existsByEmail(username)) { // 이메일이 자녀에서 발견될 경우
            return childRepository.findByEmail(username)
                    .map(this::createChildUserDetails)
                    .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
        }
        throw new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.");
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createParentUserDetails(Parent member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("Parent");

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }

    private UserDetails createChildUserDetails(Child member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("Child");

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }




}
