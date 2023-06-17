
package br.com.alura.bytebank.tests.unit.app.account;

import br.com.alura.bytebank.app.account.ListAccount;
import br.com.alura.bytebank.app.account.RegisterAccount;
import br.com.alura.bytebank.app.client.RegisterClient;
import br.com.alura.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.alura.bytebank.domain.account.AccountDto;
import br.com.alura.bytebank.domain.account.AccountRepository;
import br.com.alura.bytebank.domain.client.ClientDto;
import br.com.alura.bytebank.domain.client.ClientRepository;
import br.com.alura.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.alura.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.alura.bytebank.infra.client.ClientRepositoryMemory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAccountTest {
    private static AccountRepository accountRepository;
    private static RegisterAccount register;
    private static ClientDto clientDto;

    @BeforeAll
    static void beforeAll() {
        ClientRepository clientRepository = new ClientRepositoryMemory();
        clientDto = new ClientDto("John", "123.123.123-12", "john@email.com");
        RegisterClient registerClient = new RegisterClient(clientRepository);
        registerClient.execute(clientDto);

        accountRepository = new AccountRepositoryMemory();
        register = new RegisterAccount(accountRepository, clientRepository);
    }

    @Test
    void mustRegisterAnAccount() {
        AccountDto accountDto = new AccountDto(1234, clientDto);
        register.execute(accountDto);

        ListAccount account = new ListAccount(accountRepository);

        assertEquals(1234, account.searchByNumber(1234).number());
        assertEquals("John",account.searchByNumber(1234).client().name());
    }

    @Test
    void mustNotRegisterAccountWithNonExistentClient() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            ClientDto nonExistent = new ClientDto("nonexistent", "000.000.000-00", "nonexistent");
            AccountDto accountDto = new AccountDto(1234, nonExistent);
            register.execute(accountDto);
        });

        String expectedMessage = "There is no registered client with this CPF";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void mustNotRegisterAccountWithSameNumber() {
        Exception exception = assertThrows(AccountDomainException.class, () -> {
            AccountDto accountDto = new AccountDto(4321, clientDto);
            AccountDto accountDto1 = new AccountDto(4321, clientDto);
            register.execute(accountDto);
            register.execute(accountDto1);
        });

        String expectedMessage = "There is already an account registered with this number";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
