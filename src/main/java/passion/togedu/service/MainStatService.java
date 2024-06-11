package passion.togedu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import passion.togedu.repository.DailyQuestionRecordRepository;

@Service
@RequiredArgsConstructor
public class MainStatService {
    @Autowired
    private DailyQuestionRecordRepository dailyQuestionRecordRepository;


}
