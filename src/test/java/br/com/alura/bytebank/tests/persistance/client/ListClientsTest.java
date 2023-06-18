
package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.alura.bytebank.tests.persistance.util.ClientUtil;
import br.com.alura.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ListClientsTest {
    private static ListClient listClient;
    private static EntityManager entityManager;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");
        ClientRepository repository = RepositoryUtil.repository();
        ClientUtil.createClient(entityManager, repository);
        listClient = new ListClient(repository);
    }

    @Test
    void mustReturnAllClients() {
        assertEquals(2, listClient.list().size());
        assertEquals("John", listClient.list().get(0).name());
        assertEquals("Joanna", listClient.list().get(1).name());
    }

    @Test
    void mustReturnClientByCpf() {
        assertEquals("John", listClient.searchByCpf("123.123.123-12").name());
        assertEquals("john@email.com", listClient.searchByCpf("123.123.123-12").email());
        assertEquals("Joanna", listClient.searchByCpf("123.123.123-21").name());
    }

    @Test
    void mustReturnClientByEmail() {
        assertEquals("John", listClient.searchByEmail("john@email.com").name());
        assertEquals("123.123.123-12", listClient.searchByEmail("john@email.com").cpf());
        assertEquals("Joanna", listClient.searchByEmail("joanna@email.com").name());
    }

    @Test
    void mustNotReturnClientWithCpfNotRegistered() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            listClient.searchByCpf("123.123.123-22");
        });

        String expected = "There is no registered client with this CPF";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void mustNotReturnClientWithEmailNotRegistered() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            listClient.searchByEmail("email@email.com");
        });

        String expected = "There is no registered client with this email";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
