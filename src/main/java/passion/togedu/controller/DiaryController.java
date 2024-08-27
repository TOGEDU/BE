package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import passion.togedu.dto.diary.DiaryCalendarResponseDto;
import passion.togedu.dto.diary.DiaryCheckAvailabilityResponseDto;
import passion.togedu.dto.diary.DiaryRecordResponseDto;
import passion.togedu.dto.sign.SignUpResponseDto;
import passion.togedu.service.DiaryService;
import passion.togedu.service.WhisperService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final WhisperService whisperService;

    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudio(@RequestParam("file") MultipartFile file) {
        try {
            String transcription = whisperService.transcribe(file);
            return ResponseEntity.ok(transcription);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Transcription failed: " + e.getMessage());
        }
    }

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

    @GetMapping("/check-availability")
    public ResponseEntity<List<DiaryCheckAvailabilityResponseDto>> checkDiaryAvailability(@RequestParam("date") LocalDate date){
        Integer id = getCurrentMemberId();
        List<DiaryCheckAvailabilityResponseDto> responseDtoList = diaryService.checkDiaryAvailability(date, id);
        return ResponseEntity.ok(responseDtoList);
    }

    @PostMapping("")
    public ResponseEntity<SignUpResponseDto> createDiary(
            @RequestParam("parentChildId") Integer parentChildId,
            @RequestParam("date") LocalDate date,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image){
        Integer id = getCurrentMemberId();
        try {
            SignUpResponseDto responseDto = diaryService.createDiary(parentChildId, date, title, content, image, id);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(SignUpResponseDto.builder()
                    .success(Boolean.FALSE)
                    .msg(e.getMessage())
                    .build()
            );
        }
    }

    @PutMapping("")
    public ResponseEntity<SignUpResponseDto> updateDiary(
            @RequestParam("diaryId") Integer diaryId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image){
        try {
            SignUpResponseDto responseDto = diaryService.updateDiary(diaryId, title, content, image);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(SignUpResponseDto.builder()
                    .success(Boolean.FALSE)
                    .msg(e.getMessage())
                    .build()
            );
        }
    }
}
