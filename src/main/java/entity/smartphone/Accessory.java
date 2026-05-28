package entity.smartphone;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum Accessory {
    CASE(new BigDecimal("20")),
    EARPHONES(new BigDecimal("60")),
    CHARGER(new BigDecimal("100")),
    CHARM(new BigDecimal("20")),
    TEMPERED_GLASS(new BigDecimal("50"));

    private final BigDecimal additionalCost;
}
