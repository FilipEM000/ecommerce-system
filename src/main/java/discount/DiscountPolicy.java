package discount;

import java.math.BigDecimal;

public interface DiscountPolicy {
    BigDecimal calculateDiscount(BigDecimal totalCost);
}
