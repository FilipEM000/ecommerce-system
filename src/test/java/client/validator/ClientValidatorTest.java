package client.validator;

import exception.InvalidClientDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ClientValidatorTest {

    @Test
    void shouldPassForValidRegistrationData() {
        assertThatCode(() -> ClientValidator.validateRegistration("filip@wp.pl", "Filip"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "A"})
    void shouldThrowNameIsInvalid(String name) {
        assertThatExceptionOfType(InvalidClientDataException.class)
                .isThrownBy(() -> ClientValidator.validateRegistration("filip@wp.pl", name))
                .extracting(InvalidClientDataException::getMessage)
                .isEqualTo("Imię musi składać się z przynajmniej 2 znaków");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThatExceptionOfType(InvalidClientDataException.class)
                .isThrownBy(() -> ClientValidator.validateRegistration(null, "Filip"))
                .extracting(InvalidClientDataException::getMessage)
                .isEqualTo("Adres e-mail nie może być pusty");
    }
}
