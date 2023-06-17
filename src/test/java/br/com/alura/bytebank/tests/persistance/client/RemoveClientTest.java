package br.com.alura.bytebank.tests.persistance.client;

import br.com.alura.bytebank.app.client.ListClient;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.app.client.RemoveClient;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveClientTest {
    @Test
    void mustDeleteOneClient() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();
        ClientRepositoryDb repositoryH2 = new ClientRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto clientDto2 = new ClientDto("joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(repositoryH2);
        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        registerClient.execute(clientDto2);
        entityManager.getTransaction().commit();

        ListClient listClient = new ListClient(repositoryH2);
        RemoveClient removeClient = new RemoveClient(repositoryH2);
        entityManager.getTransaction().begin();
        removeClient.execute("123.123.123-12");
        entityManager.getTransaction().commit();

        assertEquals(1, listClient.list().size());
        entityManager.close();
    }
}
