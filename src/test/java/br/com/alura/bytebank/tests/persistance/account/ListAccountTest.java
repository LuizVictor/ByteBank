package br.com.alura.bytebank.tests.persistance.account;

import br.com.alura.bytebank.app.account.ListAccount;
import br.com.alura.bytebank.domain.account.exceptions.AccountNotFoundException;
import br.com.alura.bytebank.tests.persistance.util.AccountUtil;
import br.com.alura.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ListAccountTest {
    private static EntityManager entityManager;
    private static ListAccount listAccount;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");
        AccountUtil.create(entityManager, RepositoryUtil.accountRepository());
        listAccount = new ListAccount(RepositoryUtil.accountRepository());
    }

    @Test
    void mustListAllAccounts() {
        assertEquals(2, listAccount.list().size());
        assertEquals("John", listAccount.list().get(0).client().name());
        assertEquals("Joanna", listAccount.list().get(1).client().name());
    }

    @Test
    void mustReturnAccountByNumber() {
        assertEquals("John", listAccount.searchByNumber(1234).client().name());
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(1234).balance());
        assertEquals("Joanna", listAccount.searchByNumber(4321).client().name());
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(4321).balance());
    }

    @Test
    void mustListAccountsByClientCpf() {
        assertEquals(1, listAccount.searchByCpf("123.123.123-12").size());
        assertEquals(1, listAccount.searchByCpf("123.123.123-21").size());
        assertEquals(1234, listAccount.searchByCpf("123.123.123-12").get(0).number());
        assertEquals(4321, listAccount.searchByCpf("123.123.123-21").get(0).number());
    }

    @Test
    void mustNotReturnUnregisteredAccounts() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            listAccount.searchByNumber(1111);
        });

        String expected = "Account not exist";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void mustNotReturnAccountsWithNonExistentClient() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            listAccount.searchByCpf("123.123.123-88");
        });

        String expectedMessage = "There is no account linked to this client";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
