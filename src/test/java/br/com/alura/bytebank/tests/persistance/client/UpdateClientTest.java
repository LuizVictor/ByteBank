package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.app.client.UpdateClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateClientTest {
    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void mustUpdateClient() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();
        ClientRepositoryDb repositoryH2 = new ClientRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        RegisterClient registerClient = new RegisterClient(repositoryH2);
        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        entityManager.flush();

        ListClient listClient = new ListClient(repositoryH2);
        UpdateClient updateClient = new UpdateClient(repositoryH2);

        ClientDto clientUpdate = new ClientDto("John Doe", "123.123.123-12","email@email.com");
        updateClient.execute(clientUpdate);
        entityManager.getTransaction().commit();

        assertEquals("John Doe", listClient.searchByCpf("123.123.123-12").name());
        entityManager.close();
    }

    @AfterEach
    void tearDown() {
    }
}
