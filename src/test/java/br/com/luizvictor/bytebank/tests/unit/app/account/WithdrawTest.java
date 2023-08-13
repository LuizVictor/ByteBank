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

        AccountService accountService = new AccountServiceMemory(accountRepository);

        listAccount = new ListAccount(accountRepository);
        accountNumber = listAccount.list().get(0).number();

        Deposit deposit = new Deposit(accountRepository, accountService);
        deposit.execute(accountNumber, new BigDecimal("100"));

        withdraw = new Withdraw(accountRepository, accountService);
    }

    @Test
    void mustWithdrawTen() {
        withdraw.execute(accountNumber, BigDecimal.TEN);
        assertEquals("John", listAccount.searchByNumber(accountNumber).client().name());
        assertEquals(new BigDecimal("90"), listAccount.searchByNumber(accountNumber).balance());
    }

    @Test
    void mustNotWithAmountGreaterThaBalance() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            withdraw.execute(accountNumber, new BigDecimal("200"));
        });

        String expectedMessage = "Amount greater than balance";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotWithdrawNegativeAmount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            withdraw.execute(accountNumber, new BigDecimal("-200"));
        });

        String expectedMessage = "Cannot withdraw an amount equal to zero or a negative amount";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
