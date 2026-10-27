package client.service;

import client.dto.ClientDto;
import exception.ClientNotFoundException;
import exception.InvalidClientDataException;

public interface ClientService {
    /**
     * Registers a new client in the system.
     *
     * @param email The email address of the new client.
     * @param name  The name of the new client.
     * @return {@link ClientDto} containing the registered client's details and generated ID.
     * @throws InvalidClientDataException if the provided email or name fails validation.
     */
    ClientDto register(String email, String name);

    /**
     * Authenticates a client using their email address.
     *
     * @param email The email address used during registration.
     * @return The unique ID of the authenticated client.
     * @throws ClientNotFoundException if no client exists with the given email.
     */
    Long login(String email);

    /**
     * Deletes a client account from the system.
     *
     * @param clientId The unique ID of the client to delete.
     * @throws ClientNotFoundException if the client with the given ID does not exist.
     */
    void deleteClient(Long clientId);
}
