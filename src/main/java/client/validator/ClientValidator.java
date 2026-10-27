package client.validator;

import client.repository.ClientRepository;
import exception.EmailAlreadyExistsException;
import exception.InvalidClientDataException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Utility class for validating client-related data.
 * Ensures data integrity (like valid email format and name length) before performing business logic.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientValidator {

    public static void validateRegistration(String email, String name) {
        if (name == null || name.trim().length() < 2) {
            throw new InvalidClientDataException("Imię musi składać się z przynajmniej 2 znaków");
        }

        if (email == null || email.isBlank()) {
            throw new InvalidClientDataException("Adres e-mail nie może być pusty");
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new InvalidClientDataException("Nieprawidłowy adres email: " + email);
        }
    }

    public static void validateEmailNotTaken(String email, ClientRepository clientRepository) {
        if (clientRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Konto z adresem " + email + " już istnieje.");
        }
    }
}
