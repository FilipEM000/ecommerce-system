package discount;

import java.math.BigDecimal;

public class PercentageDiscountPolicy implements DiscountPolicy {
    private final BigDecimal discountMultiplier;

    public PercentageDiscountPolicy(double percentageOff) {
        this.discountMultiplier = BigDecimal.valueOf(percentageOff);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal totalCost) {
        return totalCost.multiply(discountMultiplier);
    }
}
