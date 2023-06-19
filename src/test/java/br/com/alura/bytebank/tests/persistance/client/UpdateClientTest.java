package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.app.client.UpdateClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.exceptions.ClientDomainException;
import br.com.alura.bytebank.tests.persistance.util.ClientUtil;
import br.com.alura.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateClientTest {
    private static ListClient listClient;
    private static UpdateClient updateClient;
    private static EntityManager entityManager;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");
        ClientRepository repository = RepositoryUtil.clientRepository();


        listClient = new ListClient(repository);
        updateClient = new UpdateClient(repository);
        ClientUtil.create(entityManager, repository);
    }

    @Test
    void mustUpdateClientName() {
        ClientDto clientUpdate = new ClientDto("John Doe", "123.123.123-12", "email@email.com");
        updateClient.execute(clientUpdate);
        entityManager.flush();

        assertEquals("John Doe", listClient.searchByCpf("123.123.123-12").name());
    }

    @Test
    void mustUpdateClientEmail() {
        ClientDto clientUpdate = new ClientDto("John", "123.123.123-12", "john@email.com");
        updateClient.execute(clientUpdate);
        entityManager.flush();

        assertEquals("John", listClient.searchByCpf("123.123.123-12").name());
        assertEquals("john@email.com", listClient.searchByCpf("123.123.123-12").email());
    }

    @Test
    void mustNotUpdateClientWithInvalidEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto clientUpdate = new ClientDto("John Doe", "123.123.123-12", "email@email");
            updateClient.execute(clientUpdate);
            entityManager.flush();
        });

        String expected = "Invalid email";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
