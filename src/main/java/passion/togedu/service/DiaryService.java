package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Child;
import passion.togedu.domain.Diary;
import passion.togedu.domain.Parent;
import passion.togedu.domain.ParentChild;
import passion.togedu.dto.diary.*;
import passion.togedu.dto.gallery.GalleryResponseDto;
import passion.togedu.dto.gallery.Photo;
import passion.togedu.dto.sign.SignUpResponseDto;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.DiaryRepository;
import passion.togedu.repository.ParentChildRepository;
import passion.togedu.repository.ParentRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final ParentChildRepository parentChildRepository;

    private final ChildRepository childRepository;

    private final ParentRepository parentRepository;

    private final S3UploadService s3UploadService;

    @Transactional
    public DiaryCalendarResponseDto getDiaryCalendar(YearMonth month, Integer parentId){
        Parent parent = parentRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
        List<Integer> parentChildIdList = parent.getParentChildList()
                .stream()
                .map(ParentChild::getId)
                .toList();

        // 시작 날짜와 끝 날짜를 계산
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        // 해당 월에 로그인된 사용자와 관련된 ParentChild ID로 일기를 가져옴.
        List<DiaryCount> diaryCountList = diaryRepository.findDiariesByParentChildIdsAndDateBetween(parentChildIdList, startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(Diary::getDate, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new DiaryCount(entry.getKey(), entry.getValue().intValue()))
                .toList();

        return DiaryCalendarResponseDto.builder()
                .month(month)
                .dateList(diaryCountList)
                .build();
    }

    @Transactional
    public List<DiaryRecordResponseDto> getDiariesByDate(LocalDate date, Integer parentId) {
        Parent parent = parentRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
        List<Integer> parentChildIdList = parent.getParentChildList()
                .stream()
                .map(ParentChild::getId)
                .toList();

        List<Diary> diaryList = diaryRepository.findDiariesByParentChildIdsAndDate(parentChildIdList, date);
        return diaryList.stream()
                .map(diary -> DiaryRecordResponseDto.builder()
                        .childName(diary.getParentChild().getChild().getName())
                        .diaryId(diary.getId())
                        .date(diary.getDate())
                        .image(diary.getImgUrl())
                        .content(diary.getContent()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DiaryCheckAvailabilityResponseDto> checkDiaryAvailability(LocalDate date, Integer parentId){
        Parent parent = parentRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."));
        List<ParentChild> parentChildList = parent.getParentChildList();
        List<Integer> parentChildIdList = parentChildList
                .stream()
                .map(ParentChild::getId)
                .toList();

        List<Integer> parentChildIdListWithDiary = diaryRepository.findParentChildIdsWithDiaryOnDate(parentChildIdList, date);

        return parentChildList.stream()
                .map(parentChild -> DiaryCheckAvailabilityResponseDto.builder()
                        .parentChildId(parentChild.getId())
                        .name(parentChild.getChild().getName())
                        .isHave(parentChildIdListWithDiary.contains(parentChild.getId()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public SignUpResponseDto createDiary(Integer parentChildId, LocalDate date,  String content, MultipartFile image, Integer parentId) throws IOException {
        if (parentChildId == -100){
            // 로그인된 사용자의 모든 자녀에게 동일한 내용의 일기를 작성함.
            List<ParentChild> parentChildList = parentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent 사용자를 찾을 수 없습니다."))
                    .getParentChildList();

            for (ParentChild parentChild: parentChildList){
                createDiaryForParentChild(parentChild, date, content, image);
            }

            return SignUpResponseDto.builder()
                    .success(Boolean.TRUE)
                    .msg("모든 자녀에게 육아일기 작성 성공")
                    .build();
        } else {
            ParentChild parentChild = parentChildRepository.findById(parentChildId)
                    .orElseThrow(() -> new RuntimeException("ParentChild 사용자를 찾을 수 없습니다."));
            createDiaryForParentChild(parentChild, date, content, image);
            return SignUpResponseDto.builder()
                    .success(Boolean.TRUE)
                    .msg("육아일기 작성 성공")
                    .build();
        }
    }

    private void createDiaryForParentChild(ParentChild parentChild, LocalDate date, String content, MultipartFile image) throws IOException {
        // 동일한 자녀와 날짜에 대해 이미 작성된 일기가 있는지 확인
        Optional<Diary> existingDiary = diaryRepository.findByParentChildIdAndDate(parentChild.getId(), date);
        if (existingDiary.isPresent()) {
            throw new RuntimeException("이미 해당 날짜에 일기가 작성되었습니다.");
        }

        String imgUrl = null;

        if (image != null && !image.isEmpty()) {
            imgUrl = s3UploadService.saveFile(image);
        }

        Diary diary = Diary.builder()
                .parentChild(parentChild)
                .date(date)
                .content(content)
                .imgUrl(imgUrl)
                .build();

        diaryRepository.save(diary);
    }

    @Transactional
    public SignUpResponseDto updateDiary(Integer diaryId, String content, MultipartFile image) throws IOException {
        // 일기 존재 여부 확인
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new RuntimeException("일기를 찾을 수 없습니다."));


        diary.setContent(content);

        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = s3UploadService.saveFile(image);
        }
        diary.setImgUrl(imageUrl);

        diaryRepository.save(diary);

        return SignUpResponseDto.builder()
                .success(Boolean.TRUE)
                .msg("육아일기 수정 성공")
                .build();
    }

    @Transactional
    public GalleryResponseDto getAllDiaryImageUrls(Integer id) {
        Child child = childRepository.findById(id).orElseThrow(()-> new RuntimeException("Child 사용자를 찾을 수 없습니다."));
        ParentChild parentChild = parentChildRepository.findByChild(child).orElseThrow(() -> new RuntimeException("Child 객체에 맞는 ParentChild 객체를 찾을 수 없습니다."));

        List<Diary> diaryList = diaryRepository.findByParentChild(parentChild);

        diaryList.sort(Comparator.comparing(Diary::getDate));

        List<Photo> photoList = new ArrayList<>();
        for (Diary diary : diaryList) {
            Photo photo = Photo.builder()
                    .date(diary.getDate())
                    .imgUrl(diary.getImgUrl())
                    .build();
            photoList.add(photo);
        }

        return GalleryResponseDto.builder()
                .photoList(photoList)
                .build();
    }
}
