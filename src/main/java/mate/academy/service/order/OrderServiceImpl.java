package mate.academy.service.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.InsufficientTokensException;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.order.OrderRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OrderResponseDto createOrder(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart for user with id: " + user.getId()
                )
        );

        if (shoppingCart.getBooks().isEmpty()) {
            throw new RuntimeException("Shopping cart is empty!");
        }

        if (!isTokensEnough(user, shoppingCart)) {
            throw new InsufficientTokensException(
                    "User with id " + user.getId() + " doesn't have enough tokens"
            );
        }

        user.setTokens(user.getTokens() - shoppingCart.getBooks().size());
        userRepository.save(user);

        Order order = new Order();
        order.setUser(user);
        order.setBooks(new ArrayList<>(shoppingCart.getBooks()));
        order.setOrderStatus(Order.OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        shoppingCart.getBooks().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(orderRepository.save(order));
    }

    private boolean isTokensEnough(User user, ShoppingCart shoppingCart) {
        int price = shoppingCart.getBooks().size();

        return user.getTokens() >= price;
    }
}
