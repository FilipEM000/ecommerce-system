package order.timeConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeConfig {
    public static final ZoneId APP_ZONE = ZoneId.of("Europe/Warsaw");
}
