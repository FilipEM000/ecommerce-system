import exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ClientRepository;
import service.impl.ClientManagerImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientManagerImplTest {

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    ClientManagerImpl clientManagerImpl;

    @Test
    void shouldDeleteClientThrowClientNotFoundException() {
        when(clientRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ClientNotFoundException.class)
                .isThrownBy(() -> clientManagerImpl.deleteClient(1))
                .extracting(ClientNotFoundException::getMessage).isEqualTo("Nie znalezioni klienta o id 1");
    }
}
