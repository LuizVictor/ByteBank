package br.com.luizvictor.bytebank.tests.unit.app.account;

import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.app.account.Transfer;
import br.com.luizvictor.bytebank.app.client.RegisterClient;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferTest {
    private static ListAccount listAccount;
    private static Transfer transfer;
    private static Integer accountWithCash;
    private static Integer accountWithoutCash;

    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        AccountRepository accountRepository = new AccountRepositoryMemory();

        AccountService accountService = new AccountServiceMemory(accountRepository);

        RegisterClient registerClient = new RegisterClient(clientRepository);
        RegisterAccount registerAccount = new RegisterAccount(accountRepository, clientRepository);

        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);

        registerAccount.execute(clientDto);
        registerAccount.execute(clientDto);
        listAccount = new ListAccount(accountRepository);
        accountWithCash = listAccount.list().get(0).number();
        accountWithoutCash = listAccount.list().get(1).number();
        Deposit deposit = new Deposit(accountRepository, accountService);
        deposit.execute(accountWithCash, new BigDecimal("100"));

        transfer = new Transfer(accountRepository, accountService);
    }

    @Test
    void mustTransferTen() {
        transfer.execute(accountWithCash, accountWithoutCash, BigDecimal.TEN);
        assertEquals(new BigDecimal("90"), listAccount.searchByNumber(accountWithCash).balance());
        assertEquals(BigDecimal.TEN, listAccount.searchByNumber(accountWithoutCash).balance());
    }

    @Test
    void mustNotTransferToSameAccount() {
        Exception exception = assertThrows(
                AccountDomainException.class,
                () -> transfer.execute(accountWithoutCash,accountWithoutCash, BigDecimal.TEN)
        );

        String expectedMessage = "Cannot transfer to the same account";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
