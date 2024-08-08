package passion.togedu.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import passion.togedu.dto.main.MainStatDto;
import passion.togedu.service.MainStatService;
import java.util.List;
import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class MainStatController {
    /*private final MainStatService mainStatService;
    //진행현황표, 오늘의 질문 조회
    @GetMapping
    public MainStatDto getHomeStats() {
        //로그인 구현 이후 고칠 예정
        Integer userId = getCurrentMemberId();
        return mainStatService.getMainStatDto(userId);
    }

    @GetMapping("/calendar")
    public List<MainCalendarDto> getCalendar(@RequestParam int year, @RequestParam int month){
        Integer userId = getCurrentMemberId();
        return mainStatService.getMainCalendarDto(userId, year, month);
    }*/

}

