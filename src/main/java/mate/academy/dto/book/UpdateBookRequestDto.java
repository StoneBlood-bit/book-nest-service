package mate.academy.dto.book;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateBookRequestDto {
    private String title;
    private String author;
    @Positive
    private int releaseYear;
    private String condition;
    private String description;
    private MultipartFile file;
    private Long genreId;
}
