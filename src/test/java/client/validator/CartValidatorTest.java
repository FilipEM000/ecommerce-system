package client.validator;

import client.entity.Cart;
import exception.InvalidProductTypeException;
import exception.InvalidQuantityException;
import exception.NotEnoughQuantityInMagazineException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import product.entity.computer.Computer;
import product.entity.smartphone.Smartphone;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CartValidatorTest {
    private Cart cart;
    private Computer computer;
    private Smartphone smartphone;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        computer = new Computer("Dell", new BigDecimal("3000"), 5);
        computer.setId(1L);
        smartphone = new Smartphone("ipPhone", new BigDecimal("4000"), 3);
        smartphone.setId(2L);
    }

    @Test
    void shouldThrowExceptionWhenQuantityToAddIsZeroOrLess() {
        assertThatExceptionOfType(InvalidQuantityException.class)
                .isThrownBy(() -> CartValidator.validateQuantityToAdd(cart, computer, 0))
                .extracting(InvalidQuantityException::getMessage)
                .isEqualTo("Ilość dodawana do koszyka musi być większa niż 0");
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughQuantityInMagazine() {
        cart.getProducts().put(computer, 4);

        assertThatExceptionOfType(NotEnoughQuantityInMagazineException.class)
                .isThrownBy(() -> CartValidator.validateQuantityToAdd(cart, computer, 2))
                .extracting(NotEnoughQuantityInMagazineException::getMessage)
                .isEqualTo("Nie masz wystarczającej ilości produktu na stanie. Dostępnych w magazynie: 5, w koszyku masz już: 4, próbujesz dodać: 2");
    }

    @Test
    void shouldCastToComputerCorrectly() {
        Computer casted = CartValidator.validateAndCastToComputer(computer);
        assertThat(casted).isNotNull();

        assertThatExceptionOfType(InvalidProductTypeException.class)
                .isThrownBy(() -> CartValidator.validateAndCastToComputer(smartphone))
                .extracting(InvalidProductTypeException::getMessage)
                .isEqualTo("Produkt nie jest komputerem");
    }
}
