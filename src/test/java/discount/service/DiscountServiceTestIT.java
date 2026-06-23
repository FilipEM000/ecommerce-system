package discount.service;

import discount.DiscountPolicy;
import discount.PercentageDiscountPolicy;
import discount.repository.impl.InMemoryDiscountRepository;
import exception.InvalidPromoCodeException;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DiscountServiceTestIT {
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        InMemoryDiscountRepository repository = new InMemoryDiscountRepository();
        discountService = new DiscountService(repository);

        discountService.addNewPromoCode("VIP10", new PercentageDiscountPolicy(new BigDecimal("0.1")));
    }

    @Test
    void shouldReturnZeroDiscountPolicyForNullOrEmptyCode() {
        DiscountPolicy nullCodePolicy = discountService.getPolicyForCode(null);
        DiscountPolicy emptyCodePolicy = discountService.getPolicyForCode("   ");

        assertThat(nullCodePolicy.calculateDiscount(new BigDecimal("1000"))).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(emptyCodePolicy.calculateDiscount(new BigDecimal("1000"))).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnCorrectPolicyForExistingCode() {
        DiscountPolicy policy = discountService.getPolicyForCode("vip10");

        BigDecimal discount = policy.calculateDiscount(new BigDecimal("1000"));

        assertThat(discount).isCloseTo(new BigDecimal("100"), Percentage.withPercentage(0.1));
    }

    @Test
    void shouldThrowExceptionForInvalidCode() {
        assertThatExceptionOfType(InvalidPromoCodeException.class)
                .isThrownBy(() -> discountService.getPolicyForCode("LOSOWY_KOD"))
                .extracting(InvalidPromoCodeException::getMessage)
                .isEqualTo("Kod rabatowy LOSOWY_KOD nie istnieje.");
    }

    @Test
    void shouldThrowExceptionWhenAddingEmptyCode() {
        assertThatExceptionOfType(InvalidPromoCodeException.class)
                .isThrownBy(() -> discountService.addNewPromoCode("", new PercentageDiscountPolicy(new BigDecimal("0.10"))))
                .extracting(InvalidPromoCodeException::getMessage)
                .isEqualTo("Kod rabatowy nie może być pusty");
    }
}
