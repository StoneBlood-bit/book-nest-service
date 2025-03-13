package mate.academy.service.order;

import mate.academy.dto.order.OrderResponseDto;
import mate.academy.model.User;

public interface OrderService {
    OrderResponseDto createOrder(User user);
}
