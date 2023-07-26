package br.com.luizvictor.bytebank.tests.unit.app.account;

import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.luizvictor.bytebank.infra.account.service.AccountServiceMemory;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryMemory;
import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.app.account.Withdraw;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawTest {
    private static ListAccount listAccount;
    private static Withdraw withdraw;


    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);
        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);

        AccountRepository accountRepository = new AccountRepositoryMemory();
        RegisterAccount openAccount = new RegisterAccount(accountRepository, clientRepository);
        AccountDto accountDto = new AccountDto(1234, clientDto);
        openAccount.execute(accountDto);

        AccountService accountService = new AccountServiceMemory(accountRepository);
        Deposit deposit = new Deposit(accountRepository, accountService);
        deposit.execute(1234, new BigDecimal("100"));

        withdraw = new Withdraw(accountRepository, accountService);
        listAccount = new ListAccount(accountRepository);
    }

    @Test
    void mustWithdrawTen() {
        withdraw.execute(1234, BigDecimal.TEN);
        assertEquals("John", listAccount.searchByNumber(1234).client().name());
        assertEquals(new BigDecimal("90"), listAccount.searchByNumber(1234).balance());
    }

    @Test
    void mustNotWithAmountGreaterThaBalance() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            withdraw.execute(1234, new BigDecimal("200"));
        });

        String expectedMessage = "Amount greater than balance";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotWithdrawNegativeAmount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            withdraw.execute(1234, new BigDecimal("-200"));
        });

        String expectedMessage = "Cannot withdraw an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
