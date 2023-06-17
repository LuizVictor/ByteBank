
package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListClientsTest {
    @BeforeAll
    static void beforeAll() {

    }

    @Test
    void mustReturnAllClients() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();
        ClientRepositoryDb repositoryH2 = new ClientRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto clientDto2 = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(repositoryH2);
        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        registerClient.execute(clientDto2);
        entityManager.getTransaction().commit();

        ListClient listClient = new ListClient(repositoryH2);

        assertEquals(2, listClient.list().size());
        assertEquals("John", listClient.list().get(0).name());
        assertEquals("Joanna", listClient.list().get(1).name());
        entityManager.close();
    }

    @Test
    void mustReturnClientByCpf() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();
        ClientRepositoryDb repositoryH2 = new ClientRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto clientDto2 = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(repositoryH2);
        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        registerClient.execute(clientDto2);
        entityManager.getTransaction().commit();

        ListClient listClient = new ListClient(repositoryH2);

        assertEquals("John", listClient.searchByCpf("123.123.123-12").name());
        assertEquals("john@email.com", listClient.searchByCpf("123.123.123-12").email());
        assertEquals("Joanna", listClient.searchByCpf("123.123.123-21").name());
        entityManager.close();
    }

    @Test
    void mustReturnClientByEmail() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();
        ClientRepositoryDb repositoryH2 = new ClientRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto clientDto2 = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(repositoryH2);
        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        registerClient.execute(clientDto2);
        entityManager.getTransaction().commit();

        ListClient listClient = new ListClient(repositoryH2);

        assertEquals("John", listClient.searchByEmail("john@email.com").name());
        assertEquals("123.123.123-12", listClient.searchByEmail("john@email.com").cpf());
        assertEquals("Joanna", listClient.searchByEmail("joanna@email.com").name());
        entityManager.close();
    }

    @AfterEach
    void tearDown() {
    }
}
