package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Child;
import passion.togedu.domain.Image;
import passion.togedu.domain.Parent;
import passion.togedu.domain.ParentChild;
import passion.togedu.dto.ChildName;
import passion.togedu.dto.mypage.ChildIdAndName;
import passion.togedu.dto.mypage.MypageChildResponseDto;
import passion.togedu.dto.mypage.MypageParentResponseDto;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.ParentChildRepository;
import passion.togedu.repository.ParentRepository;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final S3UploadService s3UploadService;
    private final ParentChildRepository parentChildRepository;
    private static final Random RANDOM = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Transactional
    public MypageParentResponseDto getParentMypage(Integer id){
        Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));

        // childList 생성
        List<ChildIdAndName> childList = new ArrayList<>();
        for (ParentChild parentChild: parent.getParentChildList()){
            ChildIdAndName childIdAndName = ChildIdAndName.builder()
                            .childId(parentChild.getChild().getId())
                                    .name(parentChild.getChild().getName())
                                            .build();
            childList.add(childIdAndName);
        }

        return MypageParentResponseDto.builder()
                .profileImage(parent.getProfileImageUrl())
                .name(parent.getName())
                .email(parent.getEmail())
                .pushNotificationTime(parent.getPushNotificationTime())
                .pushStatus(parent.getPushStatus())
                .childList(childList)
                .build();
    }

    @Transactional
    public MypageChildResponseDto getChildMypage(Integer id){
        Child child = childRepository.findById(id).orElseThrow(() -> new RuntimeException("Child 사용자를 찾을 수 없습니다."));

        return MypageChildResponseDto.builder()
                .name(child.getName())
                .email(child.getEmail())
                .pushNotificationTime(child.getPushNotificationTime())
                .pushStatus(child.getPushStatus())
                .build();
    }

    @Transactional
    public void changeProfileImage(Integer id, MultipartFile img) throws IOException {

        String imgUrl = s3UploadService.saveFile(img);

        Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));

        parent.setProfileImageUrl(imgUrl);

        parentRepository.save(parent);

    }

    @Transactional
    public void changePushNotificationTime(Integer id, String role, LocalTime pushNotificationTime){
        if (role.equals("Parent")){
            Parent parent = parentRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
            parent.setPushNotificationTime(pushNotificationTime);
            parentRepository.save(parent);
        } else if (role.equals("Child")) {
            Child child = childRepository.findById(id).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
            child.setPushNotificationTime(pushNotificationTime);
            childRepository.save(child);
        }else {
            throw new RuntimeException("사용자의 역할이 정해져 있지 않습니다.");
        }
    }

    @Transactional
    public void changeChildName(ChildIdAndName childIdAndName){
        Child child = childRepository.findById(childIdAndName.getChildId()).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
        child.setName(childIdAndName.getName());
        childRepository.save(child);
    }

    @Transactional
    public void addChild(Integer parentId, String childName){

        // Child & parentChild 생성
        Child child = Child.builder()
                .name(childName)
                .build();
        childRepository.save(child);

        ParentChild parentChild = ParentChild.builder()
                .parent(parentRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다.")))
                .child(child)
                .uniqueCode(generateUniqueString())
                .build();
        parentChildRepository.save(parentChild);

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



}
