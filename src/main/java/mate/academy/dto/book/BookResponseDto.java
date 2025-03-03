package mate.academy.dto.book;

import lombok.Data;

@Data
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String condition;
    private String genre;
    private String slug;
    private int releaseYear;
}
