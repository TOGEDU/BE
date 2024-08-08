package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import passion.togedu.dto.DailyQuestion.DailyQuestionDto;
import passion.togedu.dto.DailyQuestion.DailyQuestionRequestDto;
import passion.togedu.dto.DailyQuestion.DailyQuestionResponseDto;
import passion.togedu.service.DailyQuestionService;
import java.util.List;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/dailyquestion")
public class DailyQuestionController {
    private final DailyQuestionService dailyQuestionService;
    //목록조회
    @GetMapping("")
    public List<DailyQuestionResponseDto> getDailyQuestionList() {
        Integer userId = getCurrentMemberId();
        return dailyQuestionService.getDailyQuestionListDto(userId);
    }
    //질문조회
    @GetMapping("/today")
    public DailyQuestionDto getDailyQuestion(){
        Integer userId = getCurrentMemberId();
        return dailyQuestionService.showQuestion(userId);
    }

    @PostMapping("")
    public ResponseEntity<String> addDailyQuestion(@RequestBody DailyQuestionRequestDto dailyQuestionRequestDto){
        Integer id = getCurrentMemberId();
        dailyQuestionService.addDailyQuestionRecord(id, dailyQuestionRequestDto);
        return ResponseEntity.ok("질문 답변 추가 완료");
    }
    @PutMapping("")
    public ResponseEntity<String> changeText(@RequestBody DailyQuestionRequestDto dailyQuestionRequestDto){
        Integer id = getCurrentMemberId();
        dailyQuestionService.changeText(id, dailyQuestionRequestDto);
        return ResponseEntity.ok("질문 답변 변경 완료");
    }



}
