package mate.academy.dto.book;

import lombok.Data;

@Data
public class UpdateBookResponseDto {
    private Long id;
    private String title;
    private String author;
    private int releaseYear;
    private String condition;
    private String description;
    private byte[] image;
    private String genre;
    private String slug;
}
