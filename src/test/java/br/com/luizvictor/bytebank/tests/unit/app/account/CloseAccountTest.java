package br.com.luizvictor.bytebank.tests.unit.app.account;

import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.domain.account.AccountService;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.luizvictor.bytebank.infra.account.service.AccountServiceMemory;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryMemory;
import br.com.luizvictor.bytebank.app.account.CloseAccount;
import br.com.luizvictor.bytebank.app.account.Deposit;
import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CloseAccountTest {
    private static ListAccount listAccount;
    private static CloseAccount close;
    private static Integer firstAccountNumber;
    private static Integer secondAccountNumber;

    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        RegisterClient registerClient = new RegisterClient(clientRepository);
        ClientDto clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        registerClient.execute(clientDto);

        AccountRepository accountRepository = new AccountRepositoryMemory();
        RegisterAccount openAccount = new RegisterAccount(accountRepository, clientRepository);
        openAccount.execute(clientDto);
        openAccount.execute(clientDto);

        listAccount = new ListAccount(accountRepository);
        firstAccountNumber = listAccount.searchByCpf("123.123.123-12").get(0).number();
        secondAccountNumber = listAccount.searchByCpf("123.123.123-12").get(1).number();

        AccountService accountService = new AccountServiceMemory(accountRepository);
        Deposit deposit = new Deposit(accountRepository, accountService);
        deposit.execute(secondAccountNumber, new BigDecimal(BigInteger.TEN));

        close = new CloseAccount(accountRepository);
    }

    @Test
    void mustCloseAccount() {
        close.execute(firstAccountNumber);
        assertEquals(1, listAccount.list().size());
    }

    @Test
    void mustNotCloseAccountWithBalance() {
        Exception exception = assertThrows(AccountDomainException.class, () -> close.execute(secondAccountNumber));

        String expectedMessage = "Cannot close account with balance";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
