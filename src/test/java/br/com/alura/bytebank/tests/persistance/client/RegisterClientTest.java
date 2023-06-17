package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.exceptions.ClientDomainException;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterClientTest {
    private static RegisterClient registerClient;
    private static EntityManager entityManager;
    private static ClientRepositoryDb repositoryH2;

    @BeforeAll
    static void beforeAll() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        entityManager = factory.createEntityManager();
        repositoryH2 = new ClientRepositoryDb(entityManager);
        registerClient = new RegisterClient(repositoryH2);
    }

    @BeforeEach
    void setUp() {
        entityManager.getTransaction().begin();
    }

    @Test
    void mustRegisterClient() {
        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);
        entityManager.getTransaction().commit();
        assertEquals(1, repositoryH2.list().size());
        assertEquals("John", repositoryH2.searchByCpf("123.123.123-12").name());
        assertEquals("john@email.com", repositoryH2.searchByCpf("123.123.123-12").email());
    }

    @Test
    void mustNotRegisterClientWithInvalidCpf() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto clientDto = new ClientDto("John", "123.123.123", "john@email.com");
            registerClient.execute(clientDto);
            entityManager.flush();
        });

        String expected = "Invalid CPF";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        assertEquals(0, repositoryH2.list().size());
    }

    @Test
    void mustNotRegisterClientWithInvalidEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email");
            registerClient.execute(clientDto);
            entityManager.flush();
        });

        String expected = "Invalid email";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        assertEquals(0, repositoryH2.list().size());
    }

    @Test
    void mustNotRegisterClientWithSameCpf() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto john = new ClientDto("John", "123.123.123-12", "john@email.com");
            ClientDto joanna = new ClientDto("Joanna", "123.123.123-12", "joanna@email.com");
            registerClient.execute(john);
            registerClient.execute(joanna);
            entityManager.flush();
        });

        String expected = "There is already a client registered with this CPF";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        assertEquals(1, repositoryH2.list().size());
    }

    @Test
    void mustNotRegisterClientWithSameEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto john = new ClientDto("John", "123.123.123-12", "email@email.com");
            ClientDto joanna = new ClientDto("Joanna", "123.123.123-21", "email@email.com");
            registerClient.execute(john);
            registerClient.execute(joanna);
            entityManager.flush();
        });

        String expected = "There is already a client registered with this email";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        assertEquals(1, repositoryH2.list().size());
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }
}

