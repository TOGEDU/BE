package passion.togedu.dto.main;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 무시하도록 설정
public class MainStatDto {
    //메인화면 UserStat에 대한 정보
    private Integer answerCount;
    private Integer recordPer;
    private Integer diaryCount;
    private Integer dataAll;

    //오늘의 질문에 대한 정보?
    private String question;

}
