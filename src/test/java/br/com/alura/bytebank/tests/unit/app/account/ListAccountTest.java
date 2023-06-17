package br.com.alura.bytebank.tests.unit.app.account;

import br.com.alura.bytebank.app.account.ListAccount;
import br.com.alura.bytebank.app.account.RegisterAccount;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.account.*;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.alura.bytebank.domain.account.exceptions.AccountNotFoundException;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.alura.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class ListAccountTest {
    private static ListAccount listAccount;

    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);
        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);

        AccountRepository accountRepository = new AccountRepositoryMemory();
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);
        AccountDto account1 = new AccountDto(1234,clientDto);
        AccountDto account2 = new AccountDto(4321,clientDto);
        registerAccount.execute(account1);
        registerAccount.execute(account2);

        listAccount = new ListAccount(accountRepository);
    }

    @Test
    void mustListAllAccounts() {
        assertEquals(2, listAccount.list().size());
    }

    @Test
    void mustReturnAccountSearchedByNumber() {
        assertEquals(4321, listAccount.searchByNumber(4321).number());
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(4321).balance());
        assertEquals("John", listAccount.searchByNumber(4321).client().name());
    }

    @Test
    void mustReturnAccountSearchedByClientCpf() {
        assertEquals(2, listAccount.searchByCpf("123.123.123-12").size());
    }

    @Test
    void mustThrowExceptionIfAccountListIsEmpty() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            AccountRepository accountRepositoryEmpty = new AccountRepositoryMemory();
            ListAccount accounts = new ListAccount(accountRepositoryEmpty);
            accounts.list();
        });

        String expectedMessage = "There are no registered accounts";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotReturnUnregisteredAccounts() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            listAccount.searchByNumber(1111);
        });

        String expectedMessage = "Account not exist";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
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
}
