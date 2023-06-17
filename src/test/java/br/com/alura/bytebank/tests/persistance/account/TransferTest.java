package br.com.alura.bytebank.tests.persistance.account;

import br.com.alura.bytebank.app.account.Deposit;
import br.com.alura.bytebank.app.account.ListAccount;
import br.com.alura.bytebank.app.account.RegisterAccount;
import br.com.alura.bytebank.app.account.Transfer;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryDb;
import br.com.alura.bytebank.infra.account.service.AccountServiceDb;
import br.com.alura.bytebank.infra.client.ClientRepositoryDb;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    @Test
    void mustTransferTen() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("h2");
        EntityManager entityManager = factory.createEntityManager();

        ClientRepository clientRepository = new ClientRepositoryDb(entityManager);
        AccountRepository accountRepository = new AccountRepositoryDb(entityManager);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        RegisterClient registerClient = new RegisterClient(clientRepository);

        AccountDto accountDto1 = new AccountDto(1234, clientDto);
        AccountDto accountDto2 = new AccountDto(4321, clientDto);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);

        entityManager.getTransaction().begin();
        registerClient.execute(clientDto);
        entityManager.flush();
        registerAccount.execute(accountDto1);
        registerAccount.execute(accountDto2);
        entityManager.flush();

        AccountService accountService = new AccountServiceDb(entityManager);
        Deposit deposit = new Deposit(accountRepository, accountService);

        deposit.execute(1234, new BigDecimal("100"));
        entityManager.flush();

        ListAccount listAccount = new ListAccount(accountRepository);
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(4321).balance());

        Transfer transfer = new Transfer(accountRepository, accountService);
        transfer.execute(1234, 4321, BigDecimal.TEN);
        entityManager.getTransaction().commit();

        assertEquals("123.123.123-12", listAccount.searchByNumber(1234).client().cpf());
        assertEquals("John", listAccount.searchByNumber(1234).client().name());
        assertEquals(new BigDecimal("90"), listAccount.searchByNumber(1234).balance());
        assertEquals(BigDecimal.TEN, listAccount.searchByNumber(4321).balance());

        entityManager.close();
    }
}
