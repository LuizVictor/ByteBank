package br.com.alura.bytebank.tests.persistance.account;

import br.com.alura.bytebank.app.account.CloseAccount;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.infra.account.service.AccountServiceDb;
import br.com.alura.bytebank.tests.persistance.util.AccountUtil;
import br.com.alura.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CloseAccountTest {
    private static EntityManager entityManager;
    private static AccountRepository repository;
    private static CloseAccount close;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");

        repository = RepositoryUtil.accountRepository();
        AccountUtil.create(entityManager, repository);

        AccountService accountService = new AccountServiceDb(entityManager);
        close = new CloseAccount(repository);
    }

    @Test
    void mustCloseAccount1234() {
        close.execute(1234);
        entityManager.flush();

        assertEquals(1, AccountUtil.all(repository).size());
    }
}
