package entity.computer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum ProcessorType {
    INTEL_CORE_I5("Intel", "i5", BigDecimal.ZERO),
    INTEL_CORE_I7("Intel", "i7", new BigDecimal("200")),
    INTEL_CORE_I9("Intel", "i9", new BigDecimal("500")),
    AMD_RYZEN_5("AMD", "RYZEN 5", new BigDecimal("200")),
    AMD_RYZEN_7("AMD", "RYZEN 7", new BigDecimal("400"));

    private final String brand;
    private final String model;
    private final BigDecimal additionalCost;
}
