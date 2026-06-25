package product.validator;

import exception.InvalidPriceException;
import exception.InvalidQuantityException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class ProductValidatorTest {

    @Test
    void shouldPassForValidPrice() {
        assertThatCode(() -> ProductValidator.validatePrice(new BigDecimal("10.00")))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowWhenPriceIsZeroOrNegative() {
        assertThatExceptionOfType(InvalidPriceException.class)
                .isThrownBy(() -> ProductValidator.validatePrice(BigDecimal.ZERO))
                .extracting(InvalidPriceException::getMessage)
                .isEqualTo("Cena musi być większa od 0");

        assertThatExceptionOfType(InvalidPriceException.class)
                .isThrownBy(() -> ProductValidator.validatePrice(new BigDecimal("-5.00")))
                .extracting(InvalidPriceException::getMessage)
                .isEqualTo("Cena musi być większa od 0");
    }

    @Test
    void shouldPassForValidQuantity() {
        assertThatCode(() -> ProductValidator.validateQuantity(5))
                .doesNotThrowAnyException();

        assertThatCode(() -> ProductValidator.validateQuantity(0))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        assertThatExceptionOfType(InvalidQuantityException.class)
                .isThrownBy(() -> ProductValidator.validateQuantity(-1))
                .extracting(InvalidQuantityException::getMessage)
                .isEqualTo("Dostępna ilość produktu nie może być mniejsza niż 0");
    }
}
