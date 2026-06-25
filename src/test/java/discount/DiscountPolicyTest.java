package discount;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountPolicyTest {
    @Test
    void shouldCalculatePercentageDiscount() {
        DiscountPolicy policy = new PercentageDiscountPolicy(new BigDecimal("0.10"));
        BigDecimal cartCost = new BigDecimal("1000.00");

        BigDecimal discount = policy.calculateDiscount(cartCost);

        assertThat(discount).isCloseTo(new BigDecimal("100"), Percentage.withPercentage(0.1));
    }

    @Test
    void shouldCalculateThresholdDiscount() {
        DiscountPolicy policy = new ThresholdDiscountPolicy(new BigDecimal("500.00"), new BigDecimal("50.00"));
        BigDecimal cartCost = new BigDecimal("600.00");

        BigDecimal discount = policy.calculateDiscount(cartCost);

        assertThat(discount).isCloseTo(new BigDecimal("50"), Percentage.withPercentage(0.1));
    }

    @Test
    void shouldReturnZeroDiscountWhenCostIsBelowThreshold() {
        DiscountPolicy policy = new ThresholdDiscountPolicy(new BigDecimal("500.00"), new BigDecimal("50.00"));
        BigDecimal cartCost = new BigDecimal("499.99");

        BigDecimal discount = policy.calculateDiscount(cartCost);

        assertThat(discount).isEqualTo(BigDecimal.ZERO);
    }
}
