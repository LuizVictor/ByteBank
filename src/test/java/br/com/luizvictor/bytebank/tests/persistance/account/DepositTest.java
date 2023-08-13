package br.com.luizvictor.bytebank.tests.persistance.account;

import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.infra.account.service.AccountServiceDb;
import br.com.luizvictor.bytebank.tests.persistance.util.AccountUtil;
import br.com.luizvictor.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DepositTest {
    private static EntityManager entityManager;
    private static Deposit deposit;
    private static AccountRepository repository;
    private static Integer accountWithBalance;
    private static ListAccount listAccount;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");

        repository = RepositoryUtil.accountRepository();
        AccountUtil.create(entityManager, repository);

        AccountService accountService = new AccountServiceDb(entityManager);
        deposit = new Deposit(repository, accountService);

        listAccount = new ListAccount(repository);
        accountWithBalance = listAccount.searchByCpf("123.123.123-12").get(0).number();
    }

    @Test
    void mustDepositTen() {
        deposit.execute(accountWithBalance, BigDecimal.TEN);
        entityManager.flush();

        assertEquals(BigDecimal.TEN, AccountUtil.list(repository, accountWithBalance).balance());
    }

    @Test
    void mustNotDepositZero() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            deposit.execute(accountWithBalance, BigDecimal.ZERO);
            entityManager.flush();
        });

        String expectedMessage = "Cannot deposit an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotDepositNegativeAmount() {
        Integer accountWithoutBalance = listAccount.searchByCpf("123.123.123-21").get(0).number();

        Exception exception = assertThrows(AccountDomainException.class, () -> {
            deposit.execute(accountWithoutBalance, new BigDecimal(-111));
            entityManager.flush();
        });

        String expectedMessage = "Cannot deposit an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        assertEquals(BigDecimal.ZERO, AccountUtil.list(repository, accountWithoutBalance).balance());
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
