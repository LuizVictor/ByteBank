package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.app.client.RemoveClient;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.alura.bytebank.tests.persistance.util.ClientUtil;
import br.com.alura.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveClientTest {
    private static ListClient listClient;
    private static RemoveClient removeClient;
    private static EntityManager entityManager;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");
        ClientRepository repository = RepositoryUtil.repository();
        listClient = new ListClient(repository);
        removeClient = new RemoveClient(repository);
        ClientUtil.createClient(entityManager, repository);
    }

    @Test
    void mustDeleteOneClient() {
        removeClient.execute("123.123.123-12");
        entityManager.flush();

        assertEquals(1, listClient.list().size());
    }

    @Test
    void mustNotRemoveClientWithAnUnregisteredCpf() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
           removeClient.execute("123.123.123-22");
           entityManager.flush();
        });

        String expected = "There is no registered client with this CPF";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
