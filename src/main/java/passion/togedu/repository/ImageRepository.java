package passion.togedu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import passion.togedu.domain.Image;

// S3 테스트용 - 나중에 지워야 함
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
