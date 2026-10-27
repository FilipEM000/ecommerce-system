package discount.validator;

import discount.PercentageDiscountPolicy;
import exception.InvalidPromoCodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DiscountValidatorTest {

    @Test
    void shouldPassWhenPromoCodeIsValid() {
        assertThatCode(() -> DiscountValidator.validatePromoCode("VIP", new PercentageDiscountPolicy(new BigDecimal("0.1"))))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void shouldThrowExceptionWhenPromoCodeIsBlank(String invalidCode) {
        assertThatExceptionOfType(InvalidPromoCodeException.class)
                .isThrownBy(() -> DiscountValidator.validatePromoCode(invalidCode, new PercentageDiscountPolicy(new BigDecimal("0.1"))))
                .extracting(InvalidPromoCodeException::getMessage)
                .isEqualTo("Kod rabatowy nie może być pusty");
    }

    @Test
    void shouldThrowExceptionWhenPromoCodeIsNull() {
        assertThatExceptionOfType(InvalidPromoCodeException.class)
                .isThrownBy(() -> DiscountValidator.validatePromoCode(null, new PercentageDiscountPolicy(new BigDecimal("0.1"))))
                .extracting(InvalidPromoCodeException::getMessage)
                .isEqualTo("Kod rabatowy nie może być pusty");
    }

    @Test
    void shouldThrowExceptionWhenPolicyIsNull() {
        assertThatExceptionOfType(InvalidPromoCodeException.class)
                .isThrownBy(() -> DiscountValidator.validatePromoCode("VIP", null))
                .extracting(InvalidPromoCodeException::getMessage)
                .isEqualTo("Polityka rabatowa nie może być pusta");
    }

    @Test
    void shouldPassWhenPercentageIsValid() {
        assertThatCode(() -> DiscountValidator.validatePercentage(new BigDecimal("0.5")))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("testData")
    void shouldThrowExceptionWhenPercentageIsInvalid(BigDecimal invalidPercentage) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DiscountValidator.validatePercentage(invalidPercentage));
    }

    private static Stream<BigDecimal> testData() {
        return Stream.of(
                null,
                new BigDecimal("-0.1"),
                BigDecimal.ZERO,
                BigDecimal.ONE,
                new BigDecimal("1.5")
        );
    }

    @ParameterizedTest
    @MethodSource("testData2")
    void shouldThrowExceptionForInvalidThresholdParameters(BigDecimal threshold, BigDecimal discountAmount, String expectedMessage) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DiscountValidator.validateThreshold(threshold, discountAmount))
                .extracting(IllegalArgumentException::getMessage)
                .isEqualTo(expectedMessage);
    }

    private static Stream<Arguments> testData2() {
        return Stream.of(
                arguments(null, new BigDecimal("10"), "Próg musi być większy od 0"),
                arguments(new BigDecimal("100"), null, "Kwota rabatu musi być większa od 0"),

                arguments(BigDecimal.ZERO, new BigDecimal("10"), "Próg musi być większy od 0"),
                arguments(new BigDecimal("-50"), new BigDecimal("10"), "Próg musi być większy od 0"),
                arguments(new BigDecimal("100"), BigDecimal.ZERO, "Kwota rabatu musi być większa od 0"),
                arguments(new BigDecimal("100"), new BigDecimal("-10"), "Kwota rabatu musi być większa od 0"),

                arguments(new BigDecimal("100"), new BigDecimal("100"), "Kwota rabatu nie może być większa lub równa progowi"),
                arguments(new BigDecimal("100"), new BigDecimal("150"), "Kwota rabatu nie może być większa lub równa progowi")
        );
    }
}
