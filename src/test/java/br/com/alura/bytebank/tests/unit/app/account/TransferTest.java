package br.com.alura.bytebank.tests.unit.app.account;

import br.com.alura.bytebank.app.account.Deposit;
import br.com.alura.bytebank.app.account.ListAccount;
import br.com.alura.bytebank.app.account.RegisterAccount;
import br.com.alura.bytebank.app.account.Transfer;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.account.AccountService;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.alura.bytebank.infra.account.service.AccountServiceMemory;
import br.com.alura.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferTest {
    private static ListAccount listAccount;
    private static Transfer transfer;

    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        AccountRepository accountRepository = new AccountRepositoryMemory();

        AccountService accountService = new AccountServiceMemory(accountRepository);

        RegisterClient registerClient = new RegisterClient(clientRepository);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);

        AccountDto account1 = new AccountDto(1234, clientDto);
        registerAccount.execute(account1);

        AccountDto account2 = new AccountDto(4321, clientDto);
        registerAccount.execute(account2);

        Deposit deposit = new Deposit(accountRepository, accountService);
        deposit.execute(1234, new BigDecimal("100"));

        listAccount = new ListAccount(accountRepository);
        transfer = new Transfer(accountRepository, accountService);
    }

    @Test
    void mustTransferTen() {
        transfer.execute(1234, 4321, BigDecimal.TEN);
        assertEquals(new BigDecimal("90"), listAccount.searchByNumber(1234).balance());
        assertEquals(BigDecimal.TEN, listAccount.searchByNumber(4321).balance());
    }

    @Test
    void mustNotTransferToSameAccount() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            transfer.execute(1234,1234, BigDecimal.TEN);
        });

        String expectedMessage = "Cannot transfer to the same account";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
