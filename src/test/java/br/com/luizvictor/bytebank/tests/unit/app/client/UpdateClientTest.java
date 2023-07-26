package br.com.luizvictor.bytebank.tests.unit.app.client;

import br.com.luizvictor.bytebank.app.client.ListClient;
import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.app.client.UpdateClient;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.exceptions.ClientDomainException;
import br.com.luizvictor.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateClientTest {
    private static ListClient listClient;
    private static UpdateClient updateClient;

    @BeforeAll
    static void setUp() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);
        listClient = new ListClient(clientRepository);
        updateClient = new UpdateClient(clientRepository);

        ClientDto data1 = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto data2 = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");

        registerClient.execute(data1);
        registerClient.execute(data2);
    }

    @Test
    void mustUpdateClient() {
        ClientDto clientDto = new ClientDto("John Doe", "123.123.123-12", "email@email.com");

        updateClient.execute(clientDto);

        assertEquals("John Doe", listClient.searchByCpf("123.123.123-12").name());
        assertEquals("email@email.com", listClient.searchByCpf("123.123.123-12").email());
    }

    @Test
    void mustNotUpdateANonExistingClient() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            ClientDto clientDto = new ClientDto("John Doe","123.123.123-11", "email@email.com");
            updateClient.execute(clientDto);
        });

        String expectedMessage = "There is no registered client with this CPF";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotUpdateClientWithInvalidEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto clientDto = new ClientDto("John Doe", "123.123.123-12", "invalid@email");
            updateClient.execute(clientDto);
        });

        String expectedMessage = "Invalid email";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
