package entity.smartphone;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatteryCapacity {
    SMALL(3500),
    STANDARD(4500),
    LARGE(5500);

    private final int capacityInmAh;
}
