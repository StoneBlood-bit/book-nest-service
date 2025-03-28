package mate.academy.dto.shoppingcart;

import java.util.List;
import lombok.Data;
import mate.academy.dto.book.BookResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;

    private Long userId;

    private List<BookResponseDto> books;
}
