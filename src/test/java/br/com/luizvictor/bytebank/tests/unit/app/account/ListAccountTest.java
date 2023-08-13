package br.com.luizvictor.bytebank.tests.unit.app.account;

import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.domain.account.AccountDetailDto;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountNotFoundException;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryMemory;
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
        registerAccount.execute(clientDto);
        registerAccount.execute(clientDto);

        listAccount = new ListAccount(accountRepository);
    }

    @Test
    void mustListAllAccounts() {
        assertEquals(2, listAccount.list().size());
    }

    @Test
    void mustReturnAccountSearchedByNumber() {
        AccountDetailDto account = listAccount.searchByCpf("123.123.123-12").get(0);

        assertEquals(account.number(), listAccount.searchByNumber(account.number()).number());
        assertEquals(BigDecimal.ZERO, listAccount.searchByNumber(account.number()).balance());
        assertEquals("John", listAccount.searchByNumber(account.number()).client().name());
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
