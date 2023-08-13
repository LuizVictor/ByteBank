package br.com.luizvictor.bytebank.tests.persistance.account;

import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.domain.account.AccountDetailDto;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountNotFoundException;
import br.com.luizvictor.bytebank.tests.persistance.util.AccountUtil;
import br.com.luizvictor.bytebank.tests.persistance.util.RepositoryUtil;
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
        AccountDetailDto johnAccountNumber = listAccount.searchByCpf("123.123.123-12").get(0);
        AccountDetailDto joannaAccountNumber = listAccount.searchByCpf("123.123.123-21").get(0);
        assertEquals(2, listAccount.list().size());
        assertTrue(listAccount.list().contains(johnAccountNumber));
        assertTrue(listAccount.list().contains(joannaAccountNumber));
    }

    @Test
    void mustReturnAccountByNumber() {
        AccountDetailDto johnAccountNumber = listAccount.searchByCpf("123.123.123-12").get(0);
        AccountDetailDto joannaAccountNumber = listAccount.searchByCpf("123.123.123-21").get(0);
        assertEquals("John", listAccount.searchByNumber(johnAccountNumber.number()).client().name());
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(johnAccountNumber.number()).balance());
        assertEquals("Joanna", listAccount.searchByNumber(joannaAccountNumber.number()).client().name());
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(joannaAccountNumber.number()).balance());
    }

    @Test
    void mustListAccountsByClientCpf() {
        assertEquals(1, listAccount.searchByCpf("123.123.123-12").size());
        assertEquals(1, listAccount.searchByCpf("123.123.123-21").size());
        assertEquals("John", listAccount.searchByCpf("123.123.123-12").get(0).client().name());
        assertEquals("Joanna", listAccount.searchByCpf("123.123.123-21").get(0).client().name());
    }

    @Test
    void mustNotReturnUnregisteredAccounts() {
        Exception exception = assertThrows(
                AccountNotFoundException.class,
                () -> listAccount.searchByNumber(1111)
        );

        String expected = "Account not exist";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    void mustNotReturnAccountsWithNonExistentClient() {
        Exception exception = assertThrows(
                AccountNotFoundException.class,
                () -> listAccount.searchByCpf("123.123.123-88")
        );

        String expectedMessage = "There is no account linked to this client";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
