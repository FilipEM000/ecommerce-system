package product.entity.smartphone;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum BatteryCapacity {
    SMALL(3500, BigDecimal.ZERO),
    STANDARD(4500, new BigDecimal("50")),
    LARGE(5500, new BigDecimal("100"));

    private final Integer capacityInmAh;
    private final BigDecimal additionalCost;
}
