package order.mapper;

import client.entity.Client;
import order.dto.OrderDto;
import order.entity.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderMapperTest {

    @Test
    void shouldMapOrderToDto() {
        Client client = new Client("Filip", "filip@wp.pl");
        Order order = new Order(client, client.getCart().getProducts(), new BigDecimal("1500"));
        order.setId(1L);
        String invoiceNumber = "FV/0000/00";

        OrderDto orderDto = OrderMapper.mapToDto(order, invoiceNumber);

        assertThat(orderDto).isEqualTo(new OrderDto(
                1L,
                "Filip",
                new BigDecimal("1500"),
                order.getOrderDate(),
                "FV/0000/00"
        ));
    }
}
