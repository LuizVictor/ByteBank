package br.com.luizvictor.bytebank.tests.persistance.account;

import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.Withdraw;
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

public class WithdrawTest {
    private static EntityManager entityManager;
    private static Withdraw withdraw;
    private static AccountRepository repository;
    private static Integer accountWithBalance;
    private static Integer accountWithoutBalance;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");

        repository = RepositoryUtil.accountRepository();
        AccountUtil.create(entityManager, repository);

        AccountService accountService = new AccountServiceDb(entityManager);



        ListAccount listAccount = new ListAccount(repository);
        accountWithBalance = listAccount.searchByCpf("123.123.123-12").get(0).number();
        accountWithoutBalance = listAccount.searchByCpf("123.123.123-21").get(0).number();

        Deposit deposit = new Deposit(repository, accountService);
        deposit.execute(accountWithBalance, BigDecimal.TEN);
        entityManager.flush();

        withdraw = new Withdraw(repository, accountService);
    }

    @Test
    void mustWithdrawTen() {
        withdraw.execute(accountWithBalance, BigDecimal.TEN);
        entityManager.flush();

        assertEquals("123.123.123-12", AccountUtil.list(repository, accountWithBalance).client().cpf());
        assertEquals("John", AccountUtil.list(repository, accountWithBalance).client().name());
        assertEquals(BigDecimal.ZERO, AccountUtil.list(repository, accountWithBalance).balance());
    }

    @Test
    void mustNotWithAmountGreaterThaBalance() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            withdraw.execute(accountWithoutBalance, new BigDecimal("200"));
            entityManager.flush();
        });

        String expectedMessage = "Amount greater than balance";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        assertEquals(BigDecimal.ZERO, AccountUtil.list(repository, accountWithoutBalance).balance());
    }

    @Test
    void mustNotWithdrawNegativeAmount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            withdraw.execute(accountWithoutBalance, new BigDecimal("-200"));
            entityManager.flush();
        });

        String expectedMessage = "Cannot withdraw an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        assertEquals(BigDecimal.ZERO, AccountUtil.list(repository, accountWithoutBalance).balance());
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
