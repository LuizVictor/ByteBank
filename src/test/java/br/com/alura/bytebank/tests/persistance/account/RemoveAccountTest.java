package br.com.alura.bytebank.tests.persistance.account;

import br.com.alura.bytebank.app.account.CloseAccount;
import br.com.alura.bytebank.app.account.RegisterAccount;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryDb;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveAccountTest {
    @Test
    void mustDeleteOneAccount() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();

        ClientRepository clientRepository = new ClientRepositoryDb(entityManager);
        AccountRepository accountRepository = new AccountRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        RegisterClient registerClient = new RegisterClient(clientRepository);

        AccountDto accountDto = new AccountDto(1234, clientDto);
        AccountDto accountDto2 = new AccountDto(4321, clientDto);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);

        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        entityManager.flush();
        registerAccount.execute(accountDto);
        registerAccount.execute(accountDto2);
        entityManager.flush();

        CloseAccount closeAccount = new CloseAccount(accountRepository);
        closeAccount.execute(4321);
        entityManager.getTransaction().commit();

        assertEquals(1, accountRepository.list().size());

        entityManager.close();
    }
}
