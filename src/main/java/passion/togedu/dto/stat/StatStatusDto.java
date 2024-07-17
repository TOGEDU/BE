package passion.togedu.dto.stat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 무시하도록 설정
public class StatStatusDto {
    private LocalDate date;
    //뱃지의 유무
    private boolean badge;

}