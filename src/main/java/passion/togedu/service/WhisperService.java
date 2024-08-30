package passion.togedu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class WhisperService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.whisper-url}")
    private String whisperUrl;

    public WhisperService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String transcribe(MultipartFile audioFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 파일 리소스를 생성할 때 m4a 형식으로 지정
        Resource fileAsResource = new ByteArrayResource(audioFile.getBytes()) {
            @Override
            public String getFilename() {
                return audioFile.getOriginalFilename();
            }
        };

        // MultiValueMap에 파일과 추가 정보를 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileAsResource);  // "file" 필드에 파일 추가
        body.add("model", "whisper-1");  // Whisper 모델 정보 추가

        // HttpEntity에 헤더와 바디 설정
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Whisper API에 POST 요청 전송
        ResponseEntity<String> response = restTemplate.postForEntity(whisperUrl, requestEntity, String.class);

        // 응답 본문 반환
        return response.getBody();
    }
}
