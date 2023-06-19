package br.com.alura.bytebank.tests.persistance.account;

import br.com.alura.bytebank.app.account.Deposit;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepositTest {
    private static EntityManager entityManager;
    private static Deposit deposit;
    private static AccountRepository repository;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");

        repository = RepositoryUtil.accountRepository();
        AccountUtil.create(entityManager, repository);

        AccountService accountService = new AccountServiceDb(entityManager);
        deposit = new Deposit(repository, accountService);
    }

    @Test
    void mustDepositTen() {
        deposit.execute(1234, BigDecimal.TEN);
        entityManager.flush();

        assertEquals("123.123.123-12", AccountUtil.list(repository, 1234).client().cpf());
        assertEquals("John", AccountUtil.list(repository, 1234).client().name());
        assertEquals(BigDecimal.TEN, AccountUtil.list(repository, 1234).balance());
    }

    @Test
    void mustNotDepositZero() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            deposit.execute(1234, BigDecimal.ZERO);
        });

        String expectedMessage = "Cannot deposit an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotDepositNegativeAmount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            deposit.execute(1234, new BigDecimal(-111));
        });

        String expectedMessage = "Cannot deposit an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
