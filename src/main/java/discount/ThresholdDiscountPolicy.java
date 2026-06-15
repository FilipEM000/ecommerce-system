package discount;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public final class ThresholdDiscountPolicy implements DiscountPolicy {
    private final BigDecimal threshold;
    private final BigDecimal discountAmount;


    @Override
    public BigDecimal calculateDiscount(BigDecimal totalCost) {
        if (totalCost.compareTo(threshold) >= 0) {
            return discountAmount;
        }
        return BigDecimal.ZERO;
    }
}
