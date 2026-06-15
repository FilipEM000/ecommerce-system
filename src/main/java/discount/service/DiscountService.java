package discount.service;

import discount.DiscountPolicy;
import discount.repository.DiscountRepository;
import exception.InvalidPromoCodeException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public final class DiscountService {
    private final DiscountRepository discountRepository;


    public DiscountPolicy getPolicyForCode(String code) {
        if (code == null || code.isBlank()) {
            return totalCost -> BigDecimal.ZERO;
        }

        return discountRepository.findByCode(code)
                .orElseThrow(() -> new InvalidPromoCodeException("Kod rabatowy " + code + " nie istnieje."));
    }

    public void addNewPromoCode(String code, DiscountPolicy discountPolicy) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Kod rabatowy nie może być pusty");
        }
        discountRepository.save(code, discountPolicy);
    }
}
