package br.com.luizvictor.bytebank.tests.persistance.account;

import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.Transfer;
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

public class TransferTest {
    private static EntityManager entityManager;
    private static AccountRepository repository;
    private static Transfer transfer;
    private static Integer accountWithBalance;
    private static Integer accountWithoutBalance;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");

        repository = RepositoryUtil.accountRepository();
        AccountUtil.create(entityManager, repository);

        ListAccount listAccount = new ListAccount(repository);
        accountWithBalance = listAccount.searchByCpf("123.123.123-12").get(0).number();
        accountWithoutBalance = listAccount.searchByCpf("123.123.123-21").get(0).number();

        AccountService accountService = new AccountServiceDb(entityManager);
        Deposit deposit = new Deposit(repository, accountService);
        deposit.execute(accountWithBalance, new BigDecimal("100"));
        entityManager.flush();

        transfer = new Transfer(repository, accountService);
    }

    @Test
    void mustTransferTen() {
        transfer.execute(accountWithBalance, accountWithoutBalance, BigDecimal.TEN);
        entityManager.flush();

        assertEquals("123.123.123-12", AccountUtil.list(repository, accountWithBalance).client().cpf());
        assertEquals("John", AccountUtil.list(repository, accountWithBalance).client().name());
        assertEquals(new BigDecimal("90"), AccountUtil.list(repository, accountWithBalance).balance());
        assertEquals("123.123.123-21", AccountUtil.list(repository, accountWithoutBalance).client().cpf());
        assertEquals("Joanna", AccountUtil.list(repository, accountWithoutBalance).client().name());
        assertEquals(BigDecimal.TEN, AccountUtil.list(repository, accountWithoutBalance).balance());
    }

    @Test
    void mustNotTransferToSameAccount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            transfer.execute(accountWithBalance,accountWithBalance, BigDecimal.TEN);
            entityManager.flush();
        });

        String expectedMessage = "Cannot transfer to the same account";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        assertEquals(new BigDecimal("100"), AccountUtil.list(repository, accountWithBalance).balance());
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
