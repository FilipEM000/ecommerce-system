package order.repository;

import client.entity.Client;
import order.entity.Order;
import order.repository.impl.InMemoryOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
    }

    @Test
    void shouldSaveOrderAndAssignId() {
        Client client = new Client("Filip", "filip@wp.pl");
        Order order = new Order(client, client.getCart().getProducts(), BigDecimal.TEN);

        Order savedOrder = orderRepository.save(order);

        assertThat(savedOrder.getId()).isEqualTo(0L);
        assertThat(orderRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldRemoveOrder() {
        Client client = new Client("Filip", "filip@wp.pl");
        Order order = new Order(client, client.getCart().getProducts(), BigDecimal.TEN);

        Order savedOrder = orderRepository.save(order);
        orderRepository.remove(savedOrder);

        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());
        assertThat(foundOrder).isEmpty();
        assertThat(orderRepository.findAll()).isEmpty();
    }
}
