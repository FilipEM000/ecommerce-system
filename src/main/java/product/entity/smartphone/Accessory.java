package product.entity.smartphone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Accessory {
    CASE(new BigDecimal("20")),
    EARPHONES(new BigDecimal("60")),
    CHARGER(new BigDecimal("100")),
    CHARM(new BigDecimal("20")),
    TEMPERED_GLASS(new BigDecimal("50"));

    private final BigDecimal additionalCost;
}
