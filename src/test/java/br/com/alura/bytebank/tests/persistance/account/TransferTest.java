package br.com.alura.bytebank.tests.persistance.account;

import br.com.alura.bytebank.app.account.Deposit;
import br.com.alura.bytebank.app.account.Transfer;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.alura.bytebank.infra.account.service.AccountServiceDb;
import br.com.alura.bytebank.tests.persistance.util.AccountUtil;
import br.com.alura.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {
    private static EntityManager entityManager;
    private static AccountRepository repository;
    private static Transfer transfer;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");

        repository = RepositoryUtil.accountRepository();
        AccountUtil.create(entityManager, repository);

        AccountService accountService = new AccountServiceDb(entityManager);
        Deposit deposit = new Deposit(repository, accountService);
        deposit.execute(1234, new BigDecimal("100"));
        entityManager.flush();

        transfer = new Transfer(repository, accountService);
    }

    @Test
    void mustTransferTen() {
        transfer.execute(1234, 4321, BigDecimal.TEN);
        entityManager.flush();

        assertEquals("123.123.123-12", AccountUtil.list(repository, 1234).client().cpf());
        assertEquals("John", AccountUtil.list(repository, 1234).client().name());
        assertEquals(new BigDecimal("90"), AccountUtil.list(repository, 1234).balance());
        assertEquals("123.123.123-21", AccountUtil.list(repository, 4321).client().cpf());
        assertEquals("Joanna", AccountUtil.list(repository, 4321).client().name());
        assertEquals(BigDecimal.TEN, AccountUtil.list(repository, 4321).balance());
    }

    @Test
    void mustNotTransferToSameAccount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            transfer.execute(1234,1234, BigDecimal.TEN);
            entityManager.flush();
        });

        String expectedMessage = "Cannot transfer to the same account";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        assertEquals(new BigDecimal("100"), AccountUtil.list(repository, 1234).balance());
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
