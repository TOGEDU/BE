package passion.togedu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import passion.togedu.dto.stat.StatStatusDto;
import passion.togedu.service.StatStatusService;


import java.util.List;

import static passion.togedu.jwt.SecurityUtil.getCurrentMemberId;
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/stat")
public class StatStatusController {
    private final StatStatusService statStatusService;
    //기록 현황 api
    @GetMapping
    public List<StatStatusDto> getStatStatus() {
        Integer userId = getCurrentMemberId();
        return statStatusService.getStatStatusDto(userId);
    }

}
