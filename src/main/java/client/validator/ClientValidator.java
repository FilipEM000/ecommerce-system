package client.validator;

import client.repository.ClientRepository;
import exception.InvalidClientDataException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;
/**
 * Utility class for validating client-related data.
 * Ensures data integrity (like valid email format and name length) before performing business logic.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static void validateRegistration(String email, String name) {
        if (name == null || name.trim().length() < 2) {
            throw new InvalidClientDataException("Imię musi składać się z przynajmniej 2 znaków");
        }

        if (email == null || email.isBlank()) {
            throw new InvalidClientDataException("Adres e-mail nie może być pusty");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidClientDataException("Podany adres e-mail ma nieprawidłowy format");
        }
    }

    public static void validateEmailNotTaken(String email, ClientRepository clientRepository) {
        if (clientRepository.findByEmail(email).isPresent()) {
            throw new exception.EmailAlreadyExistsException("Konto z adresem " + email + " już istnieje.");
        }
    }
}
