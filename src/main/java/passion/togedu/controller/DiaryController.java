package passion.togedu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passion.togedu.domain.Diary;
import passion.togedu.dto.DiaryRequestDto;
import passion.togedu.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping(value = "/home", produces = "application/json")
    public ResponseEntity<Response> getDiariesByDate(@RequestParam("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Diary> diaryList = diaryService.getDiariesByDate(localDate);
        return ResponseEntity.ok(new Response(true, localDate, diaryList));
    }

    @PostMapping("")
    public ResponseEntity<String> createDiary(@RequestBody DiaryRequestDto diaryRequestDto) {
        diaryService.createDiary(diaryRequestDto);
        return ResponseEntity.ok("육아일기가 기록되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryRequestDto> updateDiary(@PathVariable int id, @RequestBody DiaryRequestDto diaryRequestDto) {
        DiaryRequestDto updatedDiaryDto = diaryService.updateDiary(id, diaryRequestDto);
        return ResponseEntity.ok(updatedDiaryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable int id) {
        diaryService.deleteDiary(id);
        return ResponseEntity.noContent().build();
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