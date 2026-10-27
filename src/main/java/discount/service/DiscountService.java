package discount.service;

import discount.DiscountPolicy;
import discount.repository.DiscountRepository;
import discount.validator.DiscountValidator;
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

        return discountRepository.findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new InvalidPromoCodeException("Kod rabatowy " + code + " nie istnieje."));
    }

    public void addNewPromoCode(String code, DiscountPolicy discountPolicy) {
        DiscountValidator.validatePromoCode(code, discountPolicy);
        discountRepository.save(code.trim().toUpperCase(), discountPolicy);
    }
}
