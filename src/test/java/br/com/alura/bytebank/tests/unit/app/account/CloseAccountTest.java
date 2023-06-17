package br.com.alura.bytebank.tests.unit.app.account;

import br.com.alura.bytebank.app.account.*;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.alura.bytebank.infra.account.service.AccountServiceMemory;
import br.com.alura.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CloseAccountTest {
    private static ListAccount listAccount;
    private static CloseAccount close;

    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);
        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);

        AccountRepository accountRepository = new AccountRepositoryMemory();
        RegisterAccount openAccount = new RegisterAccount(accountRepository, clientRepository);
        AccountDto account1 = new AccountDto(1234, clientDto);
        AccountDto account2 = new AccountDto(4321, clientDto);
        openAccount.execute(account1);
        openAccount.execute(account2);
        AccountService accountService = new AccountServiceMemory(accountRepository);
        Deposit deposit = new Deposit(accountRepository, accountService);
        deposit.execute(1234, new BigDecimal(BigInteger.TEN));

        listAccount = new ListAccount(accountRepository);
        close = new CloseAccount(accountRepository);
    }

    @Test
    void mustCloseAccount() {
        close.execute(4321);
        assertEquals(1, listAccount.list().size());
    }

    @Test
    void mustNotCloseAccountWithBalance() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            close.execute(1234);
        });

        String expectedMessage = "Cannot close account with balance";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
