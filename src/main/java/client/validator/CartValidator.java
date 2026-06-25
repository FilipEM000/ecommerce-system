package client.validator;

import client.entity.Cart;
import exception.InvalidQuantityException;
import exception.NotEnoughQuantityInMagazineException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import product.entity.Product;

import java.util.Map;

/**
 * Utility class for validating cart operations.
 * Enforces business rules such as stock availability and correct product type casting during cart additions.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartValidator {
    public static void validateQuantityToAdd(Cart cart, Product masterProduct, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new InvalidQuantityException("Ilość dodawana do koszyka musi być większa niż 0");
        }

        int alreadyInCart = cart.getProducts().entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(masterProduct.getId()))
                .mapToInt(Map.Entry::getValue)
                .sum();

        if (masterProduct.getQuantity() < (quantityToAdd + alreadyInCart)) {
            throw new NotEnoughQuantityInMagazineException("Nie masz wystarczającej ilości produktu na stanie. Dostępnych w magazynie: "
                    + masterProduct.getQuantity()
                    + ", w koszyku masz już: " + alreadyInCart
                    + ", próbujesz dodać: " + quantityToAdd);
        }
    }
}
