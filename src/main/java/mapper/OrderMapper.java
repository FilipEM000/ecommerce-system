package mapper;

import dto.OrderDto;
import entity.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderMapper {
    public static OrderDto mapToDto(Order order) {
        return new OrderDto(order.getId(), order.getClient().getName(), order.getCost());
    }
}
