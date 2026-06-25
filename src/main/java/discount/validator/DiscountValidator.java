package discount.validator;

import discount.DiscountPolicy;
import exception.InvalidPromoCodeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscountValidator {

    public static void validatePromoCode(String code, DiscountPolicy policy) {
        if (code == null || code.isBlank()) {
            throw new InvalidPromoCodeException("Kod rabatowy nie może być pusty");
        }

        if (policy == null) {
            throw new InvalidPromoCodeException("Polityka rabatowa nie może być pusta");
        }
    }

    public static void validatePercentage(BigDecimal percentageOff) {
        if (percentageOff == null || percentageOff.compareTo(BigDecimal.ZERO) <= 0 || percentageOff.compareTo(BigDecimal.ONE) >= 0) {
            throw new IllegalArgumentException(
                    "Rabat procentowy musi być z zakresu (0, 1). Podano: " + percentageOff);
        }
    }

    public static void validateThreshold(BigDecimal threshold, BigDecimal discountAmount) {
        if (threshold == null || threshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Próg musi być większy od 0");
        }
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Kwota rabatu musi być większa od 0");
        }
        if (discountAmount.compareTo(threshold) >= 0) {
            throw new IllegalArgumentException("Kwota rabatu nie może być większa lub równa progowi");
        }
    }
}
