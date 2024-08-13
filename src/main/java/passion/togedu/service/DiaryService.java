package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Child;
import passion.togedu.domain.Diary;
import passion.togedu.domain.Parent;
import passion.togedu.domain.ParentChild;
import passion.togedu.dto.diary.DiaryCalendarResponseDto;
import passion.togedu.dto.diary.DiaryCount;
import passion.togedu.dto.diary.DiaryRequestDto;
import passion.togedu.dto.gallery.GalleryResponseDto;
import passion.togedu.dto.gallery.Photo;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.DiaryRepository;
import passion.togedu.repository.ParentChildRepository;
import passion.togedu.repository.ParentRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    public Diary createDiary(DiaryRequestDto diaryRequestDto, MultipartFile file) throws IOException {
        ParentChild parentChild = parentChildRepository.findById(diaryRequestDto.getParentChildId())
                .orElseThrow(() -> new RuntimeException("ParentChild 사용자가 없습니다"));

        String imgUrl = s3UploadService.saveFile(file);

        Diary diary = Diary.builder()
                .parentChild(parentChild)
                .date(diaryRequestDto.getDate())
                .content(diaryRequestDto.getText())
                .imgUrl(imgUrl)
                .build();
        return diaryRepository.save(diary);
    }

    @Transactional
    public DiaryRequestDto updateDiary(int id, DiaryRequestDto diaryRequestDto, MultipartFile file) throws IOException {
        Diary existingDiary = getDiaryById(id);
        String imgUrl = s3UploadService.saveFile(file);

        existingDiary.setDate(diaryRequestDto.getDate());
        existingDiary.setContent(diaryRequestDto.getText());
        existingDiary.setImgUrl(imgUrl);

        diaryRepository.save(existingDiary);
        return DiaryRequestDto.fromEntity(existingDiary);
    }

    @Transactional
    public Diary getDiaryById(int id) {
        return diaryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid diary ID: " + id));
    }

    @Transactional
    public List<Diary> getDiariesByDate(LocalDate date) {
        return diaryRepository.findByDate(date);
    }

    @Transactional
    public void deleteDiary(int id) {
        diaryRepository.deleteById(id);
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
