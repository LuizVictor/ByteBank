package br.com.alura.bytebank.tests.persistance.account;

import br.com.alura.bytebank.app.account.ListAccount;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListAccountsTest {
    @Test
    void mustListAllAccounts() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();

        ClientRepository clientRepository = new ClientRepositoryDb(entityManager);
        AccountRepository accountRepository = new AccountRepositoryDb(entityManager);

        ClientDto john = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto joanna = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(clientRepository);

        AccountDto account1 = new AccountDto(1234, john);
        AccountDto account2 = new AccountDto(4321, joanna);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);

        entityManager.getTransaction().begin();
        registerClient.execute(john);
        registerClient.execute(joanna);
        entityManager.flush();
        registerAccount.execute(account1);
        registerAccount.execute(account2);
        entityManager.getTransaction().commit();

        ListAccount listAccount = new ListAccount(accountRepository);

        assertEquals(2, listAccount.list().size());
        assertEquals("John", listAccount.list().get(0).client().name());
        assertEquals("Joanna", listAccount.list().get(1).client().name());

        entityManager.close();
    }

    @Test
    void mustReturnAccountByNumber() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();

        ClientRepository clientRepository = new ClientRepositoryDb(entityManager);
        AccountRepository accountRepository = new AccountRepositoryDb(entityManager);

        ClientDto john = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto joanna = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(clientRepository);

        AccountDto account1 = new AccountDto(1234, john);
        AccountDto account2 = new AccountDto(4321, joanna);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);

        entityManager.getTransaction().begin();
        registerClient.execute(john);
        registerClient.execute(joanna);
        entityManager.flush();
        registerAccount.execute(account1);
        registerAccount.execute(account2);
        entityManager.getTransaction().commit();

        ListAccount listAccount = new ListAccount(accountRepository);

        assertEquals("John", listAccount.searchByNumber(1234).client().name());
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(1234).balance());
        assertEquals("Joanna", listAccount.searchByNumber(4321).client().name());

        entityManager.close();
    }

    @Test
    void mustListAccountsByClientCpf() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();

        ClientRepository clientRepository = new ClientRepositoryDb(entityManager);
        AccountRepository accountRepository = new AccountRepositoryDb(entityManager);

        ClientDto john = new ClientDto("John", "123.123.123-12", "john@email.com");
        ClientDto joanna = new ClientDto("Joanna", "123.123.123-21", "joanna@email.com");
        RegisterClient registerClient = new RegisterClient(clientRepository);

        AccountDto account1 = new AccountDto(1234, john);
        AccountDto account2 = new AccountDto(4321, joanna);
        AccountDto account3 = new AccountDto(3412, joanna);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);

        entityManager.getTransaction().begin();
        registerClient.execute(john);
        registerClient.execute(joanna);
        entityManager.flush();
        registerAccount.execute(account1);
        registerAccount.execute(account2);
        registerAccount.execute(account3);
        entityManager.getTransaction().commit();

        ListAccount listAccount = new ListAccount(accountRepository);

        assertEquals(2, listAccount.searchByCpf("123.123.123-21").size());
        assertEquals(1, listAccount.searchByCpf("123.123.123-12").size());
        assertEquals(3412, listAccount.searchByCpf("123.123.123-21").get(0).number());
        assertEquals(4321, listAccount.searchByCpf("123.123.123-21").get(1).number());
        entityManager.close();
    }
}
