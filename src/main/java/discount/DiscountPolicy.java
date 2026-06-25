package discount;

import java.math.BigDecimal;

/**
 * Strategy interface for calculating discounts.
 * Implementing classes define specific promotional rules (e.g., percentage-based or threshold-based discounts).
 */
public interface DiscountPolicy {
    /**
     * Calculates the discount amount based on the total cost of the order.
     *
     * @param totalCost The total cost of the cart before applying the discount.
     * @return The exact discount amount to be subtracted from the total cost.
     */
    BigDecimal calculateDiscount(BigDecimal totalCost);
}
