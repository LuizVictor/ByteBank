package br.com.luizvictor.bytebank.tests.persistance.account;

import br.com.luizvictor.bytebank.app.account.CloseAccount;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.tests.persistance.util.AccountUtil;
import br.com.luizvictor.bytebank.tests.persistance.util.RepositoryUtil;
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

        close = new CloseAccount(repository);
    }

    @Test
    void mustCloseAccount1234() {
        ListAccount listAccount = new ListAccount(repository);
        close.execute(listAccount.searchByCpf("123.123.123-12").get(0).number());
        entityManager.flush();

        assertEquals(1, AccountUtil.all(repository).size());
    }
}
