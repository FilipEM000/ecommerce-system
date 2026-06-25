package discount;

import discount.validator.DiscountValidator;

import java.math.BigDecimal;

public final class PercentageDiscountPolicy implements DiscountPolicy {
    private final BigDecimal discountMultiplier;

    public PercentageDiscountPolicy(BigDecimal  percentageOff) {
        DiscountValidator.validatePercentage(percentageOff);
        this.discountMultiplier = percentageOff;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal totalCost) {
        return totalCost.multiply(discountMultiplier)
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
