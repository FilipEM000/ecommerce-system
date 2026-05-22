package entity.computer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessorType {
    INTEL_CORE_I5("Intel", "i5"),
    INTEL_CORE_I7("Intel", "i7"),
    INTEL_CORE_I9("Intel", "i9"),
    AMD_RYZEN_5("AMD", "RYZEN 5"),
    AMD_RYZEN_7("AMD", "RYZEN 7");

    private final String brand;
    private final String model;
}
