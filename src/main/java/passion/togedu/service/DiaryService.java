package passion.togedu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passion.togedu.domain.Diary;
import passion.togedu.domain.ParentChild;
import passion.togedu.dto.DiaryRequestDto;
import passion.togedu.repository.DiaryRepository;
import passion.togedu.repository.ParentChildRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private ParentChildRepository parentChildRepository;

    // 기존 메서드
    public Diary createDiary(int parentChildId, LocalDate date, String text, String imgUrl) {
        ParentChild parentChild = parentChildRepository.findById(parentChildId).orElseThrow(() -> new RuntimeException("ParentChild 사용자가 없습니다"));
        Diary diary = Diary.builder()
                .parentChild(parentChild)
                .date(date)
                .text(text)
                .imgUrl(imgUrl)
                .build();
        return diaryRepository.save(diary);
    }

    // 새로운 메서드: DiaryRequestDto를 사용한 createDiary
    public void createDiary(DiaryRequestDto diaryRequestDto) {
        ParentChild parentChild = parentChildRepository.findById(diaryRequestDto.getParentChildId()).orElseThrow(() -> new RuntimeException("ParentChild 사용자가 없습니다"));
        Diary diary = Diary.builder()
                .parentChild(parentChild)
                .date(diaryRequestDto.getDate())
                .text(diaryRequestDto.getText())
                .imgUrl(diaryRequestDto.getImgUrl())
                .build();

        diaryRepository.save(diary);


    }

    public List<Diary> getDiariesByDate(LocalDate date) {
        // 사용자의 ParentChildCHildList를 가져욤 for 문을 생성함
        // diaryRepository.findByParentCHildAndDAte(parentCHild, date) 다 가져욤
        return diaryRepository.findByDate(date);
    }

    public Diary getDiaryById(int id) {
        return diaryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid diary ID: " + id));
    }

    public Diary saveDiary(Diary diary) {
        return diaryRepository.save(diary);
    }

    public void deleteDiary(int id) {
        diaryRepository.deleteById(id);
    }

    // 새로운 메서드: DiaryRequestDto를 사용한 updateDiary
    public DiaryRequestDto updateDiary(int id, DiaryRequestDto diaryRequestDto) {
        Diary existingDiary = getDiaryById(id);
        existingDiary.setDate(diaryRequestDto.getDate());
        existingDiary.setText(diaryRequestDto.getText());
        existingDiary.setImgUrl(diaryRequestDto.getImgUrl());
        diaryRepository.save(existingDiary);
        return DiaryRequestDto.fromEntity(existingDiary);
    }
}
