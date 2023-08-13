package br.com.luizvictor.bytebank.tests.persistance.account;

import br.com.luizvictor.bytebank.app.account.RegisterAccount;
import br.com.luizvictor.bytebank.domain.account.AccountDto;
import br.com.luizvictor.bytebank.domain.account.AccountRepository;
import br.com.luizvictor.bytebank.domain.account.exceptions.AccountDomainException;
import br.com.luizvictor.bytebank.domain.client.ClientDto;
import br.com.luizvictor.bytebank.domain.client.exceptions.ClientNotFoundException;
import br.com.luizvictor.bytebank.tests.persistance.util.ClientUtil;
import br.com.luizvictor.bytebank.tests.persistance.util.RepositoryUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAccountTest {
    private static EntityManager entityManager;
    private static AccountRepository accountRepository;
    private static RegisterAccount registerAccount;
    private static ClientDto clientDto;

    @BeforeAll
    static void beforeAll() {
        entityManager = RepositoryUtil.createEntityManager("h2");
        accountRepository = RepositoryUtil.accountRepository();
        ClientUtil.create(entityManager, RepositoryUtil.clientRepository());
        registerAccount = new RegisterAccount(accountRepository, RepositoryUtil.clientRepository());

        clientDto = ClientUtil.list(RepositoryUtil.clientRepository(), "123.123.123-12");
    }

    @Test
    void mustRegisterAccount() {
        registerAccount.execute(clientDto);
        entityManager.flush();

        Integer accountNumber = accountRepository.list().get(0).number();

        assertEquals("123.123.123-12", accountRepository.searchByNumber(accountNumber).client().cpf());
        assertEquals("John", accountRepository.searchByNumber(accountNumber).client().name());
        assertEquals(BigDecimal.ZERO, accountRepository.searchByNumber(accountNumber).balance());
    }

    @Test
    void mustNotRegisterAccountWithAClientNotRegistered() {
        Exception exception = assertThrows(ClientNotFoundException.class, () -> {
            ClientDto unregistered = new ClientDto("Unregistered", "123.123.123-22", "email@email.com");
            registerAccount.execute(unregistered);
            entityManager.flush();
        });

        String expected = "There is no registered client with this CPF";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @AfterAll
    static void afterAll() {
        entityManager.close();
    }
}
