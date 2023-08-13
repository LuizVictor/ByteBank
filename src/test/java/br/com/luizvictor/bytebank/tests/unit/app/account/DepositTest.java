package br.com.luizvictor.bytebank.tests.unit.app.account;

import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.luizvictor.bytebank.infra.account.service.AccountServiceMemory;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DepositTest {
    private static ListAccount listAccount;
    private static Deposit deposit;
    private static Integer accountNumber;

    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);
        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);

        AccountRepository accountRepository = new AccountRepositoryMemory();
        RegisterAccount openAccount = new RegisterAccount(accountRepository, clientRepository);
        openAccount.execute(clientDto);

        listAccount = new ListAccount(accountRepository);
        accountNumber = listAccount.list().get(0).number();

        AccountService accountService = new AccountServiceMemory(accountRepository);
        deposit = new Deposit(accountRepository, accountService);
    }

    @Test
    void mustDepositTen() {
        deposit.execute(accountNumber, BigDecimal.TEN);

        assertEquals("John", listAccount.searchByNumber(accountNumber).client().name());
        assertEquals(accountNumber, listAccount.searchByNumber(accountNumber).number());
        assertEquals(BigDecimal.TEN, listAccount.searchByNumber(accountNumber).balance());
    }

    @Test
    void mustDepositMoreTen() {
        deposit.execute(accountNumber, BigDecimal.TEN);

        assertEquals("John", listAccount.searchByNumber(accountNumber).client().name());
        assertEquals(accountNumber, listAccount.searchByNumber(accountNumber).number());
        assertEquals(new BigDecimal(20), listAccount.searchByNumber(accountNumber).balance());
    }


    @Test
    void mustNotDepositZero() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            deposit.execute(accountNumber, BigDecimal.ZERO);
        });

        String expectedMessage = "Cannot deposit an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotDepositNegativeAmount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            deposit.execute(accountNumber, new BigDecimal(-111));
        });

        String expectedMessage = "Cannot deposit an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
