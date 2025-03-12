package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
