package br.com.luizvictor.bytebank.tests.unit.app.client;

import br.com.luizvictor.bytebank.app.client.ListClient;
import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.app.client.RemoveClient;
import br.com.luizvictor.bytebank.domain.client.Client;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class RemoveClientTest {

    private static ClientRepository clientRepository;

    @BeforeAll
    static void setUp() {
        clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);

        ClientDto clientDto1 = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto clientDto2 = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");

        registerClient.execute(clientDto1);
        registerClient.execute(clientDto2);
    }


    @Test
    void mustRemoveOneClient() {
        RemoveClient removeClient = new RemoveClient(clientRepository);
        removeClient.execute("123.123.123-12");

        ListClient listClient = new ListClient(clientRepository);

        Client client = clientRepository.searchByCpf("123.123.123-12");
        assertNull(client);
        assertEquals(1, listClient.list().size());
    }

    @Test
    void mustNotRemoveUnregisteredClient() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            RemoveClient removeClient = new RemoveClient(clientRepository);
            removeClient.execute("123-123-123-22");
        });

        String expectedMessage = "There is no registered client with this CPF";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
