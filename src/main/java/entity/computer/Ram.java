package entity.computer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum Ram {
    DDR4_8_2400("DDR4", 8, 2400, BigDecimal.ZERO),
    DDR4_16_3200("DDR4", 16, 3200, new BigDecimal("150")),
    DDR4_32_3600("DDR4", 32, 3600, new BigDecimal("350")),
    DDR5_16_4800("DDR5", 16, 4800, new BigDecimal("500")),
    DDR5_32_5600("DDR5", 32, 5600, new BigDecimal("800")),
    DDR5_64_6000("DDR5", 64, 6000, new BigDecimal("1400"));

    private final String type;
    private final Integer capacityInGB;
    private final Integer frequencyInMHz;
    private final BigDecimal additionalCost;
}
