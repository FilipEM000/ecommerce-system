package product.validator;

import exception.InvalidPriceException;
import exception.InvalidQuantityException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Utility class for validating product-related data.
 * Protects the system against invalid domain states like negative prices or negative stock quantities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductValidator {
    public static void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidPriceException("Nie podałeś ceny");
        }

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException("Cena musi być większa od 0");
        }
    }

    public static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidQuantityException("Dostępna ilość produktu nie może być mniejsza niż 0");
        }
    }
}
