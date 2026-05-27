package client;

import exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ClientRepository;
import service.impl.ClientServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    ClientServiceImpl clientManagerImpl;

    @Test
    void shouldDeleteClientThrowClientNotFoundException() {
        when(clientRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ClientNotFoundException.class)
                .isThrownBy(() -> clientManagerImpl.deleteClient(1L))
                .extracting(ClientNotFoundException::getMessage).isEqualTo("Nie znalezioni klienta o id 1");
    }
}
