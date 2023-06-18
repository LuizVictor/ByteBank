package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.exceptions.ClientDomainException;
import br.com.alura.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterClientTest {
    private static RegisterClient registerClient;
    private static EntityManager entityManager;
    private static ClientRepository repository;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");
        repository = RepositoryUtil.repository();
        registerClient = new RegisterClient(repository);
        entityManager.getTransaction().begin();
    }

    @Test
    void mustRegisterClient() {
        ClientDto clientDto = new ClientDto("John", "123.123.123-22", "john@email.com");
        registerClient.execute(clientDto);
        entityManager.flush();
        assertEquals("John", repository.searchByCpf("123.123.123-22").name());
        assertEquals("john@email.com", repository.searchByCpf("123.123.123-22").email());
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
        assertEquals(0, repository.list().size());
    }

    @Test
    void mustNotRegisterClientWithInvalidEmail() {
        Exception exception = assertThrows(ClientDomainException.class, () -> {
            ClientDto clientDto = new ClientDto("John", "123.123.123-21", "john@email");
            registerClient.execute(clientDto);
            entityManager.flush();
        });

        String expected = "Invalid email";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
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
        assertEquals(1, repository.list().size());
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
        assertEquals(1, repository.list().size());
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}

