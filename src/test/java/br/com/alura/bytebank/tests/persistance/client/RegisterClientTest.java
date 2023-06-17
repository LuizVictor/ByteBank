package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterClientTest {
    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void mustRegisterClient() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();
        ClientRepositoryDb repositoryH2 = new ClientRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        RegisterClient registerClient = new RegisterClient(repositoryH2);
        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        entityManager.getTransaction().commit();

        assertEquals("John", repositoryH2.searchByCpf("123.123.123-12").getName());
        entityManager.close();
    }

    @Test
    void mustNotCreateClientWithInvalidCpf() {
    }

    @Test
    void mustNotCreateClientWithInvalidEmail() {
    }

    @AfterEach
    void tearDown() {
    }
}

