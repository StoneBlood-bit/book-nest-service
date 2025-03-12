package mate.academy.service.shoppingcart;

import mate.academy.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getByUserId(Long userId);

    ShoppingCart createShoppingCart(User user);

    void addBookToShoppingCart(Long bookId, Long userId);
}
