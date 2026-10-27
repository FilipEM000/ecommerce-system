package product.entity.smartphone;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BatteryCapacity {
    SMALL(3500, BigDecimal.ZERO),
    STANDARD(4500, new BigDecimal("50")),
    LARGE(5500, new BigDecimal("100"));

    private final Integer capacityInmAh;
    private final BigDecimal additionalCost;
}
