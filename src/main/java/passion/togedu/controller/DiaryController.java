package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.domain.Diary;
import passion.togedu.dto.diary.DiaryCalendarResponseDto;
import passion.togedu.dto.diary.DiaryRecordResponseDto;
import passion.togedu.dto.diary.DiaryRequestDto;
import passion.togedu.service.DiaryService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/calendar")
    public ResponseEntity<DiaryCalendarResponseDto> getDiaryCalendar(@RequestParam("month") YearMonth month){
        Integer id = getCurrentMemberId();
        DiaryCalendarResponseDto responseDto = diaryService.getDiaryCalendar(month, id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("")
    public ResponseEntity<List<DiaryRecordResponseDto>> getDiaryRecord(@RequestParam("date") LocalDate date){
        Integer id = getCurrentMemberId();
        List<DiaryRecordResponseDto> recordResponseDtoList = diaryService.getDiariesByDate(date, id);
        return ResponseEntity.ok(recordResponseDtoList);
    }

    @PostMapping("")
    public ResponseEntity<String> createDiary(@RequestPart("diaryRequestDto") DiaryRequestDto diaryRequestDto,
                                              @RequestPart("file") MultipartFile file) throws IOException {
        diaryService.createDiary(diaryRequestDto, file);
        return ResponseEntity.ok("육아일기가 기록되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDiary(@PathVariable int id,
                                              @RequestPart("diaryRequestDto") DiaryRequestDto diaryRequestDto,
                                              @RequestPart("file") MultipartFile file) throws IOException {
        diaryService.updateDiary(id, diaryRequestDto, file);
        return ResponseEntity.ok("육아일기가 수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiary(@PathVariable int id) {
        diaryService.deleteDiary(id);
        return ResponseEntity.ok("육아일기가 삭제되었습니다.");
    }

    public static class Response {
        private boolean status;
        private LocalDate date;
        private List<Diary> diaryList;

        public Response() {}

        public Response(boolean status, LocalDate date, List<Diary> diaryList) {
            this.status = status;
            this.date = date;
            this.diaryList = diaryList;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public List<Diary> getDiaryList() {
            return diaryList;
        }

        public void setDiaryList(List<Diary> diaryList) {
            this.diaryList = diaryList;
        }
    }
}
