package discount;

import discount.validator.DiscountValidator;

import java.math.BigDecimal;

public final class ThresholdDiscountPolicy implements DiscountPolicy {
    private final BigDecimal threshold;
    private final BigDecimal discountAmount;

    public ThresholdDiscountPolicy(BigDecimal threshold, BigDecimal discountAmount) {
        DiscountValidator.validateThreshold(threshold, discountAmount);
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal totalCost) {
        if (totalCost.compareTo(threshold) >= 0) {
            return discountAmount;
        }
        return BigDecimal.ZERO;
    }
}
