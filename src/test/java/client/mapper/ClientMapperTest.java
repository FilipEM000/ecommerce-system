package client.mapper;

import client.dto.ClientDto;
import client.entity.Client;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientMapperTest {

    @Test
    void shouldMapClientToDto() {
        Client client = new Client("Filip", "filip@wp.pl");
        client.setId(1L);

        ClientDto clientDto = ClientMapper.mapToDto(client);

        assertThat(clientDto).isEqualTo(new ClientDto(1L, "Filip", "filip@wp.pl"));
    }
}
