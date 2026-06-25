package client.mapper;

import client.dto.ClientDto;
import client.entity.Client;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientMapper {
    public static ClientDto mapToDto(Client client) {
        return new ClientDto(client.getId(), client.getName(), client.getEmail());
    }
}
