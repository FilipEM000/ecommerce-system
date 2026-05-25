import entity.computer.Ram;
import exception.InvalidRamFormatException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RamTest {

    @ParameterizedTest
    @MethodSource("testData")
    void shouldRamConstructorThrowInvalidRamFormatException(int capacity, int frequency, int modules){
        assertThatExceptionOfType(InvalidRamFormatException.class).isThrownBy(() -> new Ram(capacity, frequency, modules));
    }

    private static Stream<Arguments> testData(){
        return Stream.of(
                arguments(3, 4000, 2),
                arguments(7, 10, 2),
                arguments(8, 0, 2),
                arguments(8, 1000, 0)
        );
    }
}
