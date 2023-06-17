package br.com.alura.bytebank.tests.unit.app.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ListClientsTest {
    private static ListClient listClient;

    @BeforeAll
    static void setUp() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);
        listClient = new ListClient(clientRepository);

        ClientDto data1 = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto data2 = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");

        registerClient.execute(data1);
        registerClient.execute(data2);
    }

    @Test
    void mustReturnAllRegisteredClients() {
        List<ClientDto> clients = listClient.list();
        assertEquals(2, clients.size());
    }


    @Test
    void mustReturnOnlyOneClientSearchedByCpf() {
        ClientDto client = listClient.searchByCpf("123.123.123-12");
        assertEquals("John", client.name());
    }

    @Test
    void mustReturnOnlyOneClientSearchedByEmail() {
        ClientDto client = listClient.searchByEmail("joanna@email.com");
        assertEquals("Joanna", client.name());
    }


    @Test
    void mustNotReturnClientWithCpfNotRegistered() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            listClient.searchByCpf("123.123.123-22");
        });

        String expectedMessage = "There is no registered client with this CPF";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotReturnClientWithEmailNotRegistered() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            listClient.searchByEmail("invalid@email.com");
        });

        String expectedMessage = "There is no registered client with this email";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
