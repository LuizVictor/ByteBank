
package br.com.luizvictor.bytebank.tests.unit.app.account;

import br.com.luizvictor.bytebank.app.account.ListAccount;
import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.app.client.RegisterClient;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.ClientRepository;
import br.com.luizvictor.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.luizvictor.bytebank.infra.account.repository.AccountRepositoryMemory;
import br.com.luizvictor.bytebank.infra.client.ClientRepositoryMemory;
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
        register.execute(clientDto);

        ListAccount account = new ListAccount(accountRepository);

        assertEquals(1, account.searchByCpf("123.123.123-12").size());
        assertEquals(4, account.searchByCpf("123.123.123-12").get(0).number().toString().length());
        assertEquals("John", account.searchByCpf("123.123.123-12").get(0).client().name());
    }

    @Test
    void mustNotRegisterAccountWithNonExistentClient() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            ClientDto nonExistent = new ClientDto("nonexistent", "000.000.000-00", "nonexistent");
            register.execute(nonExistent);
        });

        String expectedMessage = "There is no registered client with this CPF";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}
