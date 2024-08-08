package passion.togedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Child;
import passion.togedu.domain.Diary;
import passion.togedu.domain.ParentChild;
import passion.togedu.dto.diary.DiaryRequestDto;
import passion.togedu.dto.gallery.GalleryResponseDto;
import passion.togedu.dto.gallery.Photo;
import passion.togedu.repository.ChildRepository;
import passion.togedu.repository.DiaryRepository;
import passion.togedu.repository.ParentChildRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private ParentChildRepository parentChildRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private S3UploadService s3UploadService;

    public Diary createDiary(DiaryRequestDto diaryRequestDto, MultipartFile file) throws IOException {
        ParentChild parentChild = parentChildRepository.findById(diaryRequestDto.getParentChildId())
                .orElseThrow(() -> new RuntimeException("ParentChild 사용자가 없습니다"));

        String imgUrl = s3UploadService.saveFile(file);

        Diary diary = Diary.builder()
                .parentChild(parentChild)
                .date(diaryRequestDto.getDate())
                .text(diaryRequestDto.getText())
                .imgUrl(imgUrl)
                .build();
        return diaryRepository.save(diary);
    }

    public DiaryRequestDto updateDiary(int id, DiaryRequestDto diaryRequestDto, MultipartFile file) throws IOException {
        Diary existingDiary = getDiaryById(id);
        String imgUrl = s3UploadService.saveFile(file);

        existingDiary.setDate(diaryRequestDto.getDate());
        existingDiary.setText(diaryRequestDto.getText());
        existingDiary.setImgUrl(imgUrl);

        diaryRepository.save(existingDiary);
        return DiaryRequestDto.fromEntity(existingDiary);
    }

    public Diary getDiaryById(int id) {
        return diaryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid diary ID: " + id));
    }

    public List<Diary> getDiariesByDate(LocalDate date) {
        return diaryRepository.findByDate(date);
    }

    public void deleteDiary(int id) {
        diaryRepository.deleteById(id);
    }

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
