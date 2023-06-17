package br.com.alura.bytebank.tests.unit.app.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.exceptions.ClientDomainException;
import br.com.alura.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterClientTest {
    private static ClientRepository clientRepository;
    private static RegisterClient registerClient;

    @BeforeAll
    static void beforeAll() {
        clientRepository = new ClientRepositoryMemory();
        registerClient = new RegisterClient(clientRepository);
    }

    @Test
    void mustRegisterAValidClient() {
        ClientDto data = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(data);

        ListClient client = new ListClient(clientRepository);
        assertEquals("John", client.searchByCpf("123.123.123-12").name());
    }

    @Test
    void mustNotRegisterClientsWithSameCpf() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto clientDto1 = new ClientDto("john", "123.123.123-11", "email1@email.com");
            ClientDto clientDto2 = new ClientDto("john", "123.123.123-11", "email2@email.com");

            registerClient.execute(clientDto1);
            registerClient.execute(clientDto2);
        });

        String expectedMessage = "There is already a client registered with this CPF";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void mustNotRegisterClientsWithSameEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto clientDto1 = new ClientDto("john", "123.123.123-33", "email@email.com");
            ClientDto clientDto2 = new ClientDto("john", "123.123.123-44", "email@email.com");

            registerClient.execute(clientDto1);
            registerClient.execute(clientDto2);
        });

        String expectedMessage = "There is already a client registered with this email";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
